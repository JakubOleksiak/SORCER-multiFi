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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static sorcer.eo.operator.*;
import static sorcer.so.operator.eval;
import static sorcer.so.operator.exec;

/**
 * @author Mike Sobolewski
 */
@RunWith(SorcerTestRunner.class)
@ProjectContext("examples/coffeemaker")
public class CoffeeMakerTest {
	private final static Logger logger = LoggerFactory.getLogger(CoffeeMakerTest.class);

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
	public void testContextCoffee() throws ContextException {
		assertTrue(espresso.getAmtCoffee() == 6);
	}

	@Test
	public void testContextMilk() throws ContextException {
		assertTrue(espresso.getAmtMilk() == 1);
	}

	@Test
	public void makeCoffee() throws Exception {
		coffeeMaker.addRecipe(espresso);
		assertEquals(coffeeMaker.makeCoffee(espresso, 200), 150);
	}

	//Added tests for Purchase Beverage Use Case

	@Test
	public void isMakeCoffeeNotMakingCoffeeWhenNotEnoughMoney() throws Exception {
		coffeeMaker.addRecipe(espresso);
		int chocolateBefore = inventory.getChocolate();
		int coffeeBefore = inventory.getCoffee();
		int milkBefore = inventory.getMilk();
		int sugarBefore = inventory.getSugar();
		assertEquals(coffeeMaker.makeCoffee(espresso, espresso.getPrice()-10), espresso.getPrice()-10);
		assertEquals( chocolateBefore,inventory.getChocolate());
		assertEquals(coffeeBefore,inventory.getCoffee());
		assertEquals(sugarBefore,inventory.getSugar() );
		assertEquals(milkBefore, inventory.getMilk() );

	}

	@Test
	public void isMakeCoffeeUsesIngredientsCorrectly() throws Exception {
		coffeeMaker.addRecipe(espresso);
		int chocolateBefore = inventory.getChocolate();
		int coffeeBefore = inventory.getCoffee();
		int milkBefore = inventory.getMilk();
		int sugarBefore = inventory.getSugar();
		assertEquals(coffeeMaker.makeCoffee(espresso, espresso.getPrice()), 0);
		assertEquals(chocolateBefore-espresso.getAmtChocolate(), inventory.getChocolate());
		assertEquals(coffeeBefore-espresso.getAmtCoffee(), inventory.getCoffee());
		assertEquals(sugarBefore-espresso.getAmtSugar(), inventory.getSugar());
		assertEquals(milkBefore-espresso.getAmtMilk(),inventory.getMilk());

	}

	@Test
	public void isMakeCoffeeNotMakingCoffeeWhenNotEnoughIngredients() throws Exception {
		espresso.setAmtCoffee(inventory.getCoffee()+1);
		coffeeMaker.addRecipe(espresso);
		int chocolateBefore = inventory.getChocolate();
		int coffeeBefore = inventory.getCoffee();
		int milkBefore = inventory.getMilk();
		int sugarBefore = inventory.getSugar();
		assertEquals(coffeeMaker.makeCoffee(espresso, espresso.getPrice()), espresso.getPrice());
		assertEquals( chocolateBefore,inventory.getChocolate());
		assertEquals(coffeeBefore,inventory.getCoffee());
		assertEquals(sugarBefore,inventory.getSugar() );
		assertEquals(milkBefore, inventory.getMilk() );

	}

}

