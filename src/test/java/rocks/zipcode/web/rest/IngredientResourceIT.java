package rocks.zipcode.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rocks.zipcode.domain.IngredientAsserts.*;
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
import rocks.zipcode.domain.Ingredient;
import rocks.zipcode.repository.IngredientRepository;

/**
 * Integration tests for the {@link IngredientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IngredientResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_QUANTITY = "AAAAAAAAAA";
    private static final String UPDATED_QUANTITY = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ingredients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIngredientMockMvc;

    private Ingredient ingredient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ingredient createEntity(EntityManager em) {
        Ingredient ingredient = new Ingredient().name(DEFAULT_NAME).quantity(DEFAULT_QUANTITY).unit(DEFAULT_UNIT);
        return ingredient;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ingredient createUpdatedEntity(EntityManager em) {
        Ingredient ingredient = new Ingredient().name(UPDATED_NAME).quantity(UPDATED_QUANTITY).unit(UPDATED_UNIT);
        return ingredient;
    }

    @BeforeEach
    public void initTest() {
        ingredient = createEntity(em);
    }

    @Test
    @Transactional
    void createIngredient() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Ingredient
        var returnedIngredient = om.readValue(
            restIngredientMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingredient)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Ingredient.class
        );

        // Validate the Ingredient in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIngredientUpdatableFieldsEquals(returnedIngredient, getPersistedIngredient(returnedIngredient));
    }

    @Test
    @Transactional
    void createIngredientWithExistingId() throws Exception {
        // Create the Ingredient with an existing ID
        ingredient.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIngredientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingredient)))
            .andExpect(status().isBadRequest());

        // Validate the Ingredient in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ingredient.setName(null);

        // Create the Ingredient, which fails.

        restIngredientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingredient)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ingredient.setQuantity(null);

        // Create the Ingredient, which fails.

        restIngredientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingredient)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ingredient.setUnit(null);

        // Create the Ingredient, which fails.

        restIngredientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingredient)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIngredients() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

        // Get all the ingredientList
        restIngredientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingredient.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)));
    }

    @Test
    @Transactional
    void getIngredient() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

        // Get the ingredient
        restIngredientMockMvc
            .perform(get(ENTITY_API_URL_ID, ingredient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ingredient.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT));
    }

    @Test
    @Transactional
    void getNonExistingIngredient() throws Exception {
        // Get the ingredient
        restIngredientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIngredient() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ingredient
        Ingredient updatedIngredient = ingredientRepository.findById(ingredient.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIngredient are not directly saved in db
        em.detach(updatedIngredient);
        updatedIngredient.name(UPDATED_NAME).quantity(UPDATED_QUANTITY).unit(UPDATED_UNIT);

        restIngredientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIngredient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIngredient))
            )
            .andExpect(status().isOk());

        // Validate the Ingredient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIngredientToMatchAllProperties(updatedIngredient);
    }

    @Test
    @Transactional
    void putNonExistingIngredient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingredient.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngredientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ingredient.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingredient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingredient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIngredient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingredient.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ingredient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingredient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIngredient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingredient.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingredient)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ingredient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIngredientWithPatch() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ingredient using partial update
        Ingredient partialUpdatedIngredient = new Ingredient();
        partialUpdatedIngredient.setId(ingredient.getId());

        partialUpdatedIngredient.unit(UPDATED_UNIT);

        restIngredientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngredient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIngredient))
            )
            .andExpect(status().isOk());

        // Validate the Ingredient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIngredientUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIngredient, ingredient),
            getPersistedIngredient(ingredient)
        );
    }

    @Test
    @Transactional
    void fullUpdateIngredientWithPatch() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ingredient using partial update
        Ingredient partialUpdatedIngredient = new Ingredient();
        partialUpdatedIngredient.setId(ingredient.getId());

        partialUpdatedIngredient.name(UPDATED_NAME).quantity(UPDATED_QUANTITY).unit(UPDATED_UNIT);

        restIngredientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngredient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIngredient))
            )
            .andExpect(status().isOk());

        // Validate the Ingredient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIngredientUpdatableFieldsEquals(partialUpdatedIngredient, getPersistedIngredient(partialUpdatedIngredient));
    }

    @Test
    @Transactional
    void patchNonExistingIngredient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingredient.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngredientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ingredient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ingredient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingredient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIngredient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingredient.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ingredient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingredient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIngredient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingredient.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ingredient)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ingredient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIngredient() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ingredient
        restIngredientMockMvc
            .perform(delete(ENTITY_API_URL_ID, ingredient.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ingredientRepository.count();
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

    protected Ingredient getPersistedIngredient(Ingredient ingredient) {
        return ingredientRepository.findById(ingredient.getId()).orElseThrow();
    }

    protected void assertPersistedIngredientToMatchAllProperties(Ingredient expectedIngredient) {
        assertIngredientAllPropertiesEquals(expectedIngredient, getPersistedIngredient(expectedIngredient));
    }

    protected void assertPersistedIngredientToMatchUpdatableProperties(Ingredient expectedIngredient) {
        assertIngredientAllUpdatablePropertiesEquals(expectedIngredient, getPersistedIngredient(expectedIngredient));
    }
}
