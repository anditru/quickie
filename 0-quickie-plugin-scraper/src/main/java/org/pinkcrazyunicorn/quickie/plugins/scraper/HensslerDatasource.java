package org.pinkcrazyunicorn.quickie.plugins.scraper;

import org.pinkcrazyunicorn.quickie.application.recipe.Datasource;
import org.pinkcrazyunicorn.quickie.domain.recipe.Recipe;
import org.pinkcrazyunicorn.quickie.plugins.scraper.exceptions.FailedToParseRecipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HensslerDatasource implements Datasource {
    private final HensslerScraper scraper;

    public HensslerDatasource() {
        super();
        this.scraper = new HensslerScraper();
    }

    @Override
    public Collection<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        for (String recipeURL : this.scraper.getRecipeURLs()) {
            if (recipeURL.trim().length() < 1) {
                continue;
            }
            try {
                Recipe recipe = this.scraper.getRecipeFrom(recipeURL);
                recipes.add(recipe);
                Thread.sleep(200);
            } catch (IOException e) {
                System.out.println("Warning: Could not load recipe from " + recipeURL);
            } catch (InterruptedException e) {
                System.out.println("Warning: Could not sleep after request");
            } catch (FailedToParseRecipe e) {
                System.out.println("Error: Recipe with URL " + recipeURL + " could not be parsed. The site changed its layout most likely");
            }
        }

        return recipes;
    }
}
