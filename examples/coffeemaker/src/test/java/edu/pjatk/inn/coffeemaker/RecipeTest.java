package edu.pjatk.inn.coffeemaker;

import edu.pjatk.inn.coffeemaker.impl.CoffeeMaker;
import edu.pjatk.inn.coffeemaker.impl.Inventory;
import edu.pjatk.inn.coffeemaker.impl.Recipe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sorcer.test.ProjectContext;
import org.sorcer.test.SorcerTestRunner;
import sorcer.service.ContextException;
import sorcer.service.Routine;

import static org.junit.Assert.*;
import static sorcer.eo.operator.*;
import static sorcer.so.operator.exec;

@RunWith(SorcerTestRunner.class)
@ProjectContext("examples/coffeemaker")
public class RecipeTest {
	private final static Logger logger = LoggerFactory.getLogger(RecipeTest.class);

	private CoffeeMaker coffeeMaker;
	private Inventory inventory;
	private Recipe espresso, mocha, macchiato, americano;

	@Before
	public void setUp() throws ContextException {
		coffeeMaker = new CoffeeMaker();
		inventory = coffeeMaker.checkInventory();

		espresso = new Recipe();
		espresso.setName("espresso");
		espresso.setPrice(50);
		espresso.setAmtCoffee(6);
		espresso.setAmtMilk(1);
		espresso.setAmtSugar(1);
		espresso.setAmtChocolate(0);

		mocha = new Recipe();
		mocha.setName("mocha");
		mocha.setPrice(100);
		mocha.setAmtCoffee(8);
		mocha.setAmtMilk(1);
		mocha.setAmtSugar(1);
		mocha.setAmtChocolate(2);

		macchiato = new Recipe();
		macchiato.setName("macchiato");
		macchiato.setPrice(40);
		macchiato.setAmtCoffee(7);
		macchiato.setAmtMilk(1);
		macchiato.setAmtSugar(2);
		macchiato.setAmtChocolate(0);

		americano = new Recipe();
		americano.setName("americano");
		americano.setPrice(40);
		americano.setAmtCoffee(7);
		americano.setAmtMilk(1);
		americano.setAmtSugar(2);
		americano.setAmtChocolate(0);
	}

	@Test
	public void testAddRecipe() {
		assertTrue(coffeeMaker.addRecipe(espresso));
	}

	@Test
	public void addRecipe() throws Exception {
		coffeeMaker.addRecipe(mocha);
		assertEquals(coffeeMaker.getRecipeForName("mocha").getName(), "mocha");
	}

	@Test
	public void addContextRecipe() throws Exception {
		coffeeMaker.addRecipe(Recipe.getContext(mocha));
		assertEquals(coffeeMaker.getRecipeForName("mocha").getName(), "mocha");
	}

	@Test
	public void addServiceRecipe() throws Exception {
		Routine cmt = task(sig("addRecipe", coffeeMaker),
						context(types(Recipe.class), args(espresso),
							result("recipe/added")));

		logger.info("isAdded: " + exec(cmt));
		assertEquals(coffeeMaker.getRecipeForName("espresso").getName(), "espresso");
	}

	@Test
	public void addRecipes() throws Exception {
		coffeeMaker.addRecipe(mocha);
		coffeeMaker.addRecipe(macchiato);
		coffeeMaker.addRecipe(americano);

		assertEquals(coffeeMaker.getRecipeForName("mocha").getName(), "mocha");
		assertEquals(coffeeMaker.getRecipeForName("macchiato").getName(), "macchiato");
		assertEquals(coffeeMaker.getRecipeForName("americano").getName(), "americano");
	}
	@Test
	public void isNotAbleToAddMoreThan3Recipes() throws Exception {
		coffeeMaker.addRecipe(mocha);
		coffeeMaker.addRecipe(macchiato);
		coffeeMaker.addRecipe(americano);

		assertEquals(coffeeMaker.getRecipeForName("mocha").getName(), "mocha");
		assertEquals(coffeeMaker.getRecipeForName("macchiato").getName(), "macchiato");
		assertEquals(coffeeMaker.getRecipeForName("americano").getName(), "americano");

		Recipe additionalRecipe = new Recipe();
		additionalRecipe.setName("Additional");
		additionalRecipe.setPrice(35);
		additionalRecipe.setAmtCoffee(6);
		additionalRecipe.setAmtMilk(2);
		additionalRecipe.setAmtSugar(4);
		additionalRecipe.setAmtChocolate(2);
		assertFalse(coffeeMaker.addRecipe(additionalRecipe));
	}

	@Test
	public void isEachRecipeUnique() throws Exception {
		coffeeMaker.addRecipe(mocha);
		assertEquals(coffeeMaker.getRecipeForName(mocha.getName()).getName(), mocha.getName());
		assertFalse(coffeeMaker.addRecipe(mocha));
	}

	@Test
	public void isRecipeDeleted() throws Exception {
		coffeeMaker.addRecipe(mocha);
		assertEquals(coffeeMaker.getRecipeForName(mocha.getName()).getName(), mocha.getName());
		coffeeMaker.addRecipe(americano);
		assertEquals(coffeeMaker.getRecipeForName(americano.getName()).getName(), americano.getName());

		assertTrue(coffeeMaker.deleteRecipe(mocha));
		assertNull(coffeeMaker.getRecipeForName(mocha.getName()));
	}

	@Test
	public void isRecipeEdited() throws Exception {
		coffeeMaker.addRecipe(mocha);
		assertEquals(coffeeMaker.getRecipeForName(mocha.getName()).getName(), mocha.getName());

		Recipe mochaEdited = new Recipe();
		mochaEdited.setName(mocha.getName()+"Edited");
		mochaEdited.setPrice(mocha.getPrice()+20);
		mochaEdited.setAmtChocolate(mocha.getAmtChocolate());
		mochaEdited.setAmtMilk(mocha.getAmtMilk());
		mochaEdited.setAmtSugar(mocha.getAmtSugar());
		mochaEdited.setAmtCoffee(mocha.getAmtCoffee());

		assertTrue(coffeeMaker.editRecipe(coffeeMaker.getRecipeForName(mocha.getName()),mochaEdited));
		assertEquals(coffeeMaker.getRecipeForName(mochaEdited.getName()).getName(), mochaEdited.getName());
		assertEquals(coffeeMaker.getRecipeForName(mochaEdited.getName()).getPrice(), mochaEdited.getPrice());
		assertNull(coffeeMaker.getRecipeForName(mocha.getName()));

	}

	@Test
	public void isRecipeNameEditToOtherInTheListNotAllowed() throws Exception {
		coffeeMaker.addRecipe(mocha);
		assertEquals(coffeeMaker.getRecipeForName(mocha.getName()).getName(), mocha.getName());
		coffeeMaker.addRecipe(espresso);
		assertEquals(coffeeMaker.getRecipeForName(espresso.getName()).getName(), espresso.getName());

		Recipe mochaEdited = new Recipe();
		mochaEdited.setName(espresso.getName());
		mochaEdited.setPrice(mocha.getPrice());
		mochaEdited.setAmtChocolate(mocha.getAmtChocolate());
		mochaEdited.setAmtMilk(mocha.getAmtMilk());
		mochaEdited.setAmtSugar(mocha.getAmtSugar());
		mochaEdited.setAmtCoffee(mocha.getAmtCoffee());

		assertFalse(coffeeMaker.editRecipe(coffeeMaker.getRecipeForName(mocha.getName()),mochaEdited));
	}



}

