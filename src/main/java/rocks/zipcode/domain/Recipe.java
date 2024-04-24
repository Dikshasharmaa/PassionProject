package rocks.zipcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Recipe.
 */
@Entity
@Table(name = "recipe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Recipe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "cuisine")
    private String cuisine;

    @Column(name = "difficulty_level")
    private String difficultyLevel;

    @Column(name = "preparation_time")
    private Integer preparationTime;

    @Column(name = "cooking_time")
    private Integer cookingTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User author;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipe")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "recipe" }, allowSetters = true)
    private Set<Ingredient> ingredients = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipe")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "recipe" }, allowSetters = true)
    private Set<Instruction> instructions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipe")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "recipe" }, allowSetters = true)
    private Set<Video> videos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Recipe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Recipe title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Recipe description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCuisine() {
        return this.cuisine;
    }

    public Recipe cuisine(String cuisine) {
        this.setCuisine(cuisine);
        return this;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getDifficultyLevel() {
        return this.difficultyLevel;
    }

    public Recipe difficultyLevel(String difficultyLevel) {
        this.setDifficultyLevel(difficultyLevel);
        return this;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Integer getPreparationTime() {
        return this.preparationTime;
    }

    public Recipe preparationTime(Integer preparationTime) {
        this.setPreparationTime(preparationTime);
        return this;
    }

    public void setPreparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
    }

    public Integer getCookingTime() {
        return this.cookingTime;
    }

    public Recipe cookingTime(Integer cookingTime) {
        this.setCookingTime(cookingTime);
        return this;
    }

    public void setCookingTime(Integer cookingTime) {
        this.cookingTime = cookingTime;
    }

    public User getAuthor() {
        return this.author;
    }

    public void setAuthor(User user) {
        this.author = user;
    }

    public Recipe author(User user) {
        this.setAuthor(user);
        return this;
    }

    public Set<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        if (this.ingredients != null) {
            this.ingredients.forEach(i -> i.setRecipe(null));
        }
        if (ingredients != null) {
            ingredients.forEach(i -> i.setRecipe(this));
        }
        this.ingredients = ingredients;
    }

    public Recipe ingredients(Set<Ingredient> ingredients) {
        this.setIngredients(ingredients);
        return this;
    }

    public Recipe addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        ingredient.setRecipe(this);
        return this;
    }

    public Recipe removeIngredient(Ingredient ingredient) {
        this.ingredients.remove(ingredient);
        ingredient.setRecipe(null);
        return this;
    }

    public Set<Instruction> getInstructions() {
        return this.instructions;
    }

    public void setInstructions(Set<Instruction> instructions) {
        if (this.instructions != null) {
            this.instructions.forEach(i -> i.setRecipe(null));
        }
        if (instructions != null) {
            instructions.forEach(i -> i.setRecipe(this));
        }
        this.instructions = instructions;
    }

    public Recipe instructions(Set<Instruction> instructions) {
        this.setInstructions(instructions);
        return this;
    }

    public Recipe addInstruction(Instruction instruction) {
        this.instructions.add(instruction);
        instruction.setRecipe(this);
        return this;
    }

    public Recipe removeInstruction(Instruction instruction) {
        this.instructions.remove(instruction);
        instruction.setRecipe(null);
        return this;
    }

    public Set<Video> getVideos() {
        return this.videos;
    }

    public void setVideos(Set<Video> videos) {
        if (this.videos != null) {
            this.videos.forEach(i -> i.setRecipe(null));
        }
        if (videos != null) {
            videos.forEach(i -> i.setRecipe(this));
        }
        this.videos = videos;
    }

    public Recipe videos(Set<Video> videos) {
        this.setVideos(videos);
        return this;
    }

    public Recipe addVideo(Video video) {
        this.videos.add(video);
        video.setRecipe(this);
        return this;
    }

    public Recipe removeVideo(Video video) {
        this.videos.remove(video);
        video.setRecipe(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recipe)) {
            return false;
        }
        return getId() != null && getId().equals(((Recipe) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Recipe{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", cuisine='" + getCuisine() + "'" +
            ", difficultyLevel='" + getDifficultyLevel() + "'" +
            ", preparationTime=" + getPreparationTime() +
            ", cookingTime=" + getCookingTime() +
            "}";
    }
}
