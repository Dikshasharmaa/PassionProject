package rocks.zipcode.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rocks.zipcode.domain.InstructionAsserts.*;
import static rocks.zipcode.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import rocks.zipcode.IntegrationTest;
import rocks.zipcode.domain.Instruction;
import rocks.zipcode.repository.InstructionRepository;

/**
 * Integration tests for the {@link InstructionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InstructionResourceIT {

    private static final Integer DEFAULT_STEP_NUMBER = 1;
    private static final Integer UPDATED_STEP_NUMBER = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/instructions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InstructionRepository instructionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInstructionMockMvc;

    private Instruction instruction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instruction createEntity(EntityManager em) {
        Instruction instruction = new Instruction().stepNumber(DEFAULT_STEP_NUMBER).description(DEFAULT_DESCRIPTION);
        return instruction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instruction createUpdatedEntity(EntityManager em) {
        Instruction instruction = new Instruction().stepNumber(UPDATED_STEP_NUMBER).description(UPDATED_DESCRIPTION);
        return instruction;
    }

    @BeforeEach
    public void initTest() {
        instruction = createEntity(em);
    }

    @Test
    @Transactional
    void createInstruction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Instruction
        var returnedInstruction = om.readValue(
            restInstructionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(instruction)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Instruction.class
        );

        // Validate the Instruction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInstructionUpdatableFieldsEquals(returnedInstruction, getPersistedInstruction(returnedInstruction));
    }

    @Test
    @Transactional
    void createInstructionWithExistingId() throws Exception {
        // Create the Instruction with an existing ID
        instruction.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstructionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(instruction)))
            .andExpect(status().isBadRequest());

        // Validate the Instruction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStepNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        instruction.setStepNumber(null);

        // Create the Instruction, which fails.

        restInstructionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(instruction)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        instruction.setDescription(null);

        // Create the Instruction, which fails.

        restInstructionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(instruction)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInstructions() throws Exception {
        // Initialize the database
        instructionRepository.saveAndFlush(instruction);

        // Get all the instructionList
        restInstructionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instruction.getId().intValue())))
            .andExpect(jsonPath("$.[*].stepNumber").value(hasItem(DEFAULT_STEP_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getInstruction() throws Exception {
        // Initialize the database
        instructionRepository.saveAndFlush(instruction);

        // Get the instruction
        restInstructionMockMvc
            .perform(get(ENTITY_API_URL_ID, instruction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(instruction.getId().intValue()))
            .andExpect(jsonPath("$.stepNumber").value(DEFAULT_STEP_NUMBER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingInstruction() throws Exception {
        // Get the instruction
        restInstructionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInstruction() throws Exception {
        // Initialize the database
        instructionRepository.saveAndFlush(instruction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the instruction
        Instruction updatedInstruction = instructionRepository.findById(instruction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInstruction are not directly saved in db
        em.detach(updatedInstruction);
        updatedInstruction.stepNumber(UPDATED_STEP_NUMBER).description(UPDATED_DESCRIPTION);

        restInstructionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInstruction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInstruction))
            )
            .andExpect(status().isOk());

        // Validate the Instruction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInstructionToMatchAllProperties(updatedInstruction);
    }

    @Test
    @Transactional
    void putNonExistingInstruction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        instruction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstructionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, instruction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(instruction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instruction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInstruction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        instruction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstructionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(instruction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instruction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInstruction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        instruction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstructionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(instruction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Instruction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInstructionWithPatch() throws Exception {
        // Initialize the database
        instructionRepository.saveAndFlush(instruction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the instruction using partial update
        Instruction partialUpdatedInstruction = new Instruction();
        partialUpdatedInstruction.setId(instruction.getId());

        partialUpdatedInstruction.stepNumber(UPDATED_STEP_NUMBER);

        restInstructionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstruction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInstruction))
            )
            .andExpect(status().isOk());

        // Validate the Instruction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInstructionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInstruction, instruction),
            getPersistedInstruction(instruction)
        );
    }

    @Test
    @Transactional
    void fullUpdateInstructionWithPatch() throws Exception {
        // Initialize the database
        instructionRepository.saveAndFlush(instruction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the instruction using partial update
        Instruction partialUpdatedInstruction = new Instruction();
        partialUpdatedInstruction.setId(instruction.getId());

        partialUpdatedInstruction.stepNumber(UPDATED_STEP_NUMBER).description(UPDATED_DESCRIPTION);

        restInstructionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstruction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInstruction))
            )
            .andExpect(status().isOk());

        // Validate the Instruction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInstructionUpdatableFieldsEquals(partialUpdatedInstruction, getPersistedInstruction(partialUpdatedInstruction));
    }

    @Test
    @Transactional
    void patchNonExistingInstruction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        instruction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstructionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, instruction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(instruction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instruction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInstruction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        instruction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstructionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(instruction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instruction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInstruction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        instruction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstructionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(instruction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Instruction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInstruction() throws Exception {
        // Initialize the database
        instructionRepository.saveAndFlush(instruction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the instruction
        restInstructionMockMvc
            .perform(delete(ENTITY_API_URL_ID, instruction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return instructionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Instruction getPersistedInstruction(Instruction instruction) {
        return instructionRepository.findById(instruction.getId()).orElseThrow();
    }

    protected void assertPersistedInstructionToMatchAllProperties(Instruction expectedInstruction) {
        assertInstructionAllPropertiesEquals(expectedInstruction, getPersistedInstruction(expectedInstruction));
    }

    protected void assertPersistedInstructionToMatchUpdatableProperties(Instruction expectedInstruction) {
        assertInstructionAllUpdatablePropertiesEquals(expectedInstruction, getPersistedInstruction(expectedInstruction));
    }
}
