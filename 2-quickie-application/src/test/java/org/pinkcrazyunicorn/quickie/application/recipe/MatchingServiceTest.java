package org.pinkcrazyunicorn.quickie.application.recipe;

import static org.assertj.core.api.Assertions.*;
import org.easymock.EasyMock;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.pinkcrazyunicorn.quickie.domain.Food;
import org.pinkcrazyunicorn.quickie.domain.profile.Opinion;
import org.pinkcrazyunicorn.quickie.domain.profile.Profile;
import org.pinkcrazyunicorn.quickie.domain.recipe.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class MatchingServiceTest {
    @ParameterizedTest
    @MethodSource("getTestData")
    public void shouldFindExpectedMatchingRecipes(Profile profile, RecipeService recipeService,
                                          Collection<Recipe> expected, String origin, Collection<Object> toVerify) {
        MatchingService codeUnderTest = new MatchingService(recipeService);

        Collection<Recipe> actual = codeUnderTest.getMatchingRecipesFor(origin, profile);

        assertThat(actual).hasSize(expected.size())
                .containsAll(expected);
        for (Object mock : toVerify) {
            EasyMock.verify(mock);
        }
    }

    static Stream<Arguments> getTestData() {
        Quantity commonQuantity = new Quantity(new Unit("commonUnit"), 1.0);

        Profile profile = EasyMock.createMock(Profile.class);

        Food dealbreakerFood = new Food("Dealbreaker");
        Ingredient dealbreakerIngredient = new Ingredient(dealbreakerFood, commonQuantity);
        EasyMock.expect(profile.getOpinionAbout(dealbreakerFood)).andReturn(Optional.of(Opinion.Dealbreaker)).atLeastOnce();

        Food dislikedFood = new Food("Disliked");
        Ingredient dislikedIngredient = new Ingredient(dislikedFood, commonQuantity);
        EasyMock.expect(profile.getOpinionAbout(dislikedFood)).andReturn(Optional.of(Opinion.Dislike)).atLeastOnce();

        Food commonFood = new Food("common");
        EasyMock.expect(profile.getOpinionAbout(commonFood)).andReturn(Optional.empty()).atLeastOnce();
        Ingredient commonIngredient = new Ingredient(commonFood, commonQuantity);
        Recipe recipe = EasyMock.createMock(Recipe.class);
        EasyMock.expect(recipe.getIngredients()).andReturn(List.of(commonIngredient)).atLeastOnce();
        EasyMock.replay(recipe);

        Recipe dislikedRecipe = EasyMock.createMock(Recipe.class);
        EasyMock.expect(dislikedRecipe.getIngredients()).andReturn(List.of(commonIngredient, dislikedIngredient)).atLeastOnce();
        EasyMock.replay(dislikedRecipe);

        Recipe dealbreakerRecipe = EasyMock.createMock(Recipe.class);
        EasyMock.expect(dealbreakerRecipe.getIngredients()).andReturn(List.of(commonIngredient, dealbreakerIngredient)).atLeastOnce();
        EasyMock.replay(dealbreakerRecipe);

        EasyMock.replay(profile);

        RecipeService recipeService1 = EasyMock.createMock(RecipeService.class);
        EasyMock.expect(recipeService1.getAll()).andReturn(List.of(
                dislikedRecipe, dealbreakerRecipe,
                recipe, recipe, recipe, recipe, recipe,
                recipe, recipe, recipe, recipe, recipe
        ));
        EasyMock.expect(recipeService1.getBy("Default")).andReturn(Optional.of(recipe));
        EasyMock.replay(recipeService1);

        RecipeService recipeService2 = EasyMock.createMock(RecipeService.class);
        EasyMock.expect(recipeService2.getAll()).andReturn(List.of(
                dislikedRecipe, dealbreakerRecipe,
                recipe, recipe, recipe, recipe, recipe,
                recipe, recipe, recipe, recipe
        ));
        EasyMock.expect(recipeService2.getBy("Default")).andReturn(Optional.of(recipe));
        EasyMock.replay(recipeService2);

        RecipeService recipeService3 = EasyMock.createMock(RecipeService.class);
        EasyMock.expect(recipeService3.getAll()).andReturn(List.of(
                dislikedRecipe, dealbreakerRecipe,
                recipe, recipe, recipe, recipe, recipe,
                recipe, recipe, recipe
        ));
        EasyMock.expect(recipeService3.getBy("Default")).andReturn(Optional.of(recipe));
        EasyMock.replay(recipeService3);

        RecipeService recipeService4 = EasyMock.createMock(RecipeService.class);
        EasyMock.expect(recipeService4.getAll()).andReturn(List.of(
                dislikedRecipe, dealbreakerRecipe,
                recipe, recipe, recipe, recipe, recipe,
                recipe, recipe
        ));
        EasyMock.expect(recipeService4.getBy("Default")).andReturn(Optional.of(recipe));
        EasyMock.replay(recipeService4);

        RecipeService recipeService5 = EasyMock.createMock(RecipeService.class);
        EasyMock.expect(recipeService5.getAll()).andReturn(List.of(recipe));
        EasyMock.expect(recipeService5.getBy("Default")).andReturn(Optional.of(recipe));
        EasyMock.replay(recipeService5);

        RecipeService recipeService6 = EasyMock.createMock(RecipeService.class);
        EasyMock.expect(recipeService6.getAll()).andReturn(List.of());
        EasyMock.expect(recipeService6.getBy("Default")).andReturn(Optional.of(recipe));
        EasyMock.replay(recipeService6);

        RecipeService recipeService7 = EasyMock.createMock(RecipeService.class);
        EasyMock.expect(recipeService7.getBy("Invalid")).andReturn(Optional.empty());
        EasyMock.replay(recipeService7);

        return Stream.of(
            Arguments.of(profile, recipeService1,
                    List.of(recipe, recipe, recipe, recipe, recipe,
                            recipe, recipe, recipe, recipe, recipe),
                    "Default",
                    List.of(profile, recipeService1, dealbreakerRecipe, dislikedRecipe, recipe)),
            Arguments.of(profile, recipeService2,
                    List.of(recipe, recipe, recipe, recipe, recipe,
                            recipe, recipe, recipe, recipe, dislikedRecipe),
                    "Default",
                    List.of(profile, recipeService2, dealbreakerRecipe, dislikedRecipe, recipe)),
            Arguments.of(profile, recipeService3,
                    List.of(recipe, recipe, recipe, recipe, recipe,
                            recipe, recipe, recipe, dislikedRecipe, dealbreakerRecipe),
                    "Default",
                    List.of(profile, recipeService3, dealbreakerRecipe, dislikedRecipe, recipe)),
            Arguments.of(profile, recipeService4,
                    List.of(recipe, recipe, recipe, recipe, recipe,
                            recipe, recipe, dislikedRecipe, dealbreakerRecipe),
                    "Default",
                    List.of(profile, recipeService4, dealbreakerRecipe, dislikedRecipe, recipe)),
            Arguments.of(profile, recipeService5,
                    List.of(recipe),
                    "Default",
                    List.of(profile, recipeService5, dealbreakerRecipe, dislikedRecipe, recipe)),
            Arguments.of(profile, recipeService6,
                    List.of(),
                    "Default",
                    List.of(profile, recipeService6, dealbreakerRecipe, dislikedRecipe, recipe)),
            Arguments.of(profile, recipeService7,
                    List.of(),
                    "Invalid",
                    List.of(profile, recipeService7, dealbreakerRecipe, dislikedRecipe, recipe))
        );
    }
}
