package edu.pjatk.inn.coffeemaker;

import edu.pjatk.inn.coffeemaker.impl.CoffeeMaker;
import edu.pjatk.inn.coffeemaker.impl.DeliveryImpl;
import edu.pjatk.inn.coffeemaker.impl.Recipe;
import edu.pjatk.inn.requestor.CoffeemakerConsumer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sorcer.test.ProjectContext;
import org.sorcer.test.SorcerTestRunner;
import sorcer.core.provider.rendezvous.ServiceJobber;
import sorcer.service.*;

import static edu.pjatk.inn.coffeemaker.impl.Recipe.getRecipe;
import static org.junit.Assert.*;
import static sorcer.co.operator.*;
import static sorcer.ent.operator.*;
import static sorcer.eo.operator.alt;
import static sorcer.eo.operator.args;
import static sorcer.eo.operator.opt;
import static sorcer.eo.operator.pipe;
import static sorcer.eo.operator.result;
import static sorcer.eo.operator.*;
import static sorcer.mo.operator.add;
import static sorcer.mo.operator.model;
import static sorcer.mo.operator.result;
import static sorcer.mo.operator.value;
import static sorcer.mo.operator.*;
import static sorcer.so.operator.*;

@RunWith(SorcerTestRunner.class)
@ProjectContext("examples/coffeemaker")
public class NewGenCoffeServiceTest {
    private final static Logger logger = LoggerFactory.getLogger(NewGenCoffeServiceTest.class);

    private Context espresso, mocha, americano, coffeemaker;

    @Before
    public void setUp() throws ContextException {
        espresso = context(ent("key", "espresso"), ent("price", 50),
                ent("amtCoffee", 6), ent("amtMilk", 0),
                ent("amtSugar", 1), ent("amtChocolate", 0));

        mocha = context(ent("key", "mocha"), ent("price", 100),
                ent("amtCoffee", 8), ent("amtMilk", 1),
                ent("amtSugar", 1), ent("amtChocolate", 2));

        americano = context(ent("key", "americano"), ent("price", 40),
                ent("amtCoffee", 4), ent("amtMilk", 0),
                ent("amtSugar", 1), ent("amtChocolate", 0));

		Context inventory = context(ent("coffeeAmt", 50), ent("milkAmt", 50), ent("sugarAmt", 50), ent("chocolateAmt", 50));
		coffeemaker = context(ent("id", "hall-03"), ent("inventory", inventory));
    }

    @After
    public void cleanUp() throws Exception {
        Routine cmt =
                task(sig("removeRecipes", INextGenCoffemakerService.class),
                        context(types(), args()));

        cmt = exert(cmt);
        logger.info("deleted recipes context: " + context(cmt));
    }

    @Test
    public void giveCode() throws Exception {
        Routine addMocha = task(sig("addRecipe", INextGenCoffemakerService.class), mocha.addPcr(ent("user-id", "s18728")), result("recipes/s18728/mocha/added"));
        Routine createOrder = task(sig("createOrder", INextGenCoffemakerService.class), inVal("recipe", "recipes/s18728/mocha"), result("coffeemakers/hall-03/orders/s18728/01"));
        Routine giveCode = task(sig("giveCode", IAuthenticationService.class), inVal("order", "coffeemakers/hall-03/orders/s18728/01"), outPaths("coffeemakers/hall-03/orders/s18728/01/isCodeGiven", "coffeemakers/hall-03/orders/s18728/01/code"));
        Block block = block("giveCode",addMocha,createOrder, giveCode);
        Context out = context(exert(block));
        assertEquals(value(out, "coffeemakers/hall-03/orders/s18728/01/isCodeGiven"), true);
        assertNotNull(value(out, "coffeemakers/hall-03/orders/s18728/01/code"));

    }

    @Test
    public void checkCode() throws Exception {
        Routine addMocha = task(sig("addRecipe", INextGenCoffemakerService.class), mocha.addPcr(ent("user-id", "s18728")), result("recipes/s18728/mocha/added"));
        Routine createOrder = task(sig("createOrder", INextGenCoffemakerService.class), inVal("recipe", "recipes/s18728/mocha"), result("coffeemakers/hall-03/orders/s18728/01"));
        Routine giveCode = task(sig("giveCode", IAuthenticationService.class), inVal("order", "coffeemakers/hall-03/orders/s18728/01"), outPaths("coffeemakers/hall-03/orders/s18728/01/isCodeGiven", "coffeemakers/hall-03/orders/s18728/01/code"));
        Routine checkCode = task(sig("checkCode", IAuthenticationService.class), inVal("order", "coffeemakers/hall-03/orders/s18728/01"), inVal("code", "coffeemakers/hall-03/orders/s18728/01/code"), result("coffeemakers/hall-03/orders/s18728/01/isCodeValid"));
        Block block = block("checkCode",addMocha,createOrder, giveCode, checkCode);
        Context out = context(exert(block));
        assertEquals(value(out, "coffeemakers/hall-03/orders/s18728/01/isCodeValid"), true);
    }

    @Test
    public void addRecipe() throws Exception {
        Routine addEspresso = task(sig("addRecipe", INextGenCoffemakerService.class), espresso.addPcr(ent("user-id", "s18728")), outPaths("recipes/s18728/espresso/added"));
        Routine addAmericano = task(sig("addRecipe", INextGenCoffemakerService.class), americano.addPcr(ent("user-id", "s18728")), outPaths("recipes/s18728/americano/added"));

        Routine getRecipes = task(sig("getRecipes", INextGenCoffemakerService.class), outPaths("recipes/s18728/all"));

        Block addAndCheckRecipes = block(addEspresso, addAmericano, getRecipes);
        Context out = context(exert(addAndCheckRecipes));

        assertEquals(value(out, "recipes/s18728/espresso/added"), true);
        assertEquals(value(out, "recipes/s18728/americano/added"), true);
        assertNotNull(value(out, "recipes/s18728/all"));
    }

    @Test
    public void createOrder() throws Exception {
        Routine addMocha = task(sig("addRecipe", INextGenCoffemakerService.class), mocha.addPcr(ent("user-id", "s18725")), result("recipes/s18725/mocha/added"));
        Routine createOrder = task(sig("createOrder", INextGenCoffemakerService.class), inVal("recipe", "recipes/s18725/mocha"), result("coffeemakers/hall-03/orders/s18725/01"));
        Block block = block("createOrder",addMocha,createOrder);
        Context out = context(exert(block));
        assertNotNull(value(out, "coffeemakers/hall-03/orders/s18725/01"));
    }

    @Test
    public void makeCoffee() throws Exception {
        Routine addMocha = task(sig("addRecipe", INextGenCoffemakerService.class), mocha.addPcr(ent("user-id", "s18725")), result("recipes/s18728/mocha/added"));
        Routine makeCoffee = task(sig("makeCoffee", INextGenCoffemakerService.class), inVal("recipe", "recipes/s18725/mocha"), coffeemaker);
        Block block = block("makeCoffee",addMocha,makeCoffee);
        Context out = context(exert(block));
        assertEquals(value(out, "coffeemakers/hall-03/orders/s18728/01/isMade"), true);
    }

    @Test
    public void storeCoffee() throws Exception {
        Routine addMocha = task(sig("addRecipe", INextGenCoffemakerService.class), mocha.addPcr(ent("user-id", "s18725")), result("recipes/s18728/mocha/added"));
        Routine makeCoffee = task(sig("makeCoffee", INextGenCoffemakerService.class), inVal("recipe", "recipes/s18725/mocha"), coffeemaker, result("coffeemakers/hall-03/orders/s18725/01/isMade"));
        Routine storeCoffee = task(sig("storeCoffee", INextGenCoffemakerService.class), inVal("order", "coffeemakers/hall-03/orders/s18725/01"));
        Block block = block("storeCoffee",addMocha,makeCoffee,storeCoffee);
        Context out = context(exert(block));
        assertEquals(value(out, "coffeemakers/hall-03/orders/s18725/01/isStored"), true);
    }

    @Test
    public void giveCoffee() throws Exception {
        Routine addMocha = task(sig("addRecipe", INextGenCoffemakerService.class), mocha.addPcr(ent("user-id", "s18725")), result("recipes/s18725/mocha/added"));
        Routine makeCoffee = task(sig("makeCoffee", INextGenCoffemakerService.class), inVal("recipe", "recipes/s18725/mocha"), coffeemaker, result("coffeemakers/hall-03/orders/s18725/01/isMade"));
        Routine giveCoffee = task(sig("giveCoffee", INextGenCoffemakerService.class), inVal("order", "coffeemakers/hall-03/orders/s18728/01"), result("coffeemakers/hall-03/orders/s18728/01/isGiven"));
        Block block = block("giveCoffee",addMocha,makeCoffee,giveCoffee);
        Context out = context(exert(block));
        assertEquals(value(out, "coffeemakers/hall-03/orders/s18725/01/isGiven"), true);
    }

    @Test
    public void confirmPayment() throws Exception {
        Routine addMocha = task(sig("addRecipe", INextGenCoffemakerService.class), mocha.addPcr(ent("user-id", "s18725")), result("recipes/s18725/mocha/added"));
        Routine createOrder = task(sig("createOrder", INextGenCoffemakerService.class), inVal("recipe", "recipes/s18725/mocha"), result("coffeemakers/hall-03/orders/s18725/01"));
        Routine confirmPayment = task(sig("confirmPayment", IPaymentService.class), inVal("order", "coffeemakers/hall-03/orders/s18725/01"), result("coffeemakers/hall-03/orders/s18725/01/isPayedFor"));
        Block block = block("confirmPayment",addMocha,createOrder,confirmPayment);
        Context out = context(exert(block));
        assertEquals(value(out, "coffeemakers/hall-03/orders/s18725/01/isPayedFor"), true);
    }

    @Test
    public void checkInventory() throws Exception {
        Routine addMocha = task(sig("addRecipe", INextGenCoffemakerService.class), mocha.addPcr(ent("user-id", "s18725")), result("recipes/s18725/mocha/added"));
        Routine createOrder = task(sig("createOrder", INextGenCoffemakerService.class), inVal("recipe", "recipes/s18725/mocha"), result("coffeemakers/hall-03/orders/s18725/01"));
        Routine checkInventory = task(sig("checkInventory", IInventoryService.class), inVal("order", "coffeemakers/hall-03/orders/s18725/01"), result("coffeemakers/hall-03/orders/s18725/01/isEnough"));
        Block block = block("giveCoffee",addMocha,createOrder,checkInventory);
        Context out = context(exert(block));
        assertEquals(value(out, "coffeemakers/hall-03/orders/s18725/01/isEnough"), true);
    }

	@Test
	public void terminateOrder() throws Exception{
		Routine addMocha = task(sig("addRecipe", INextGenCoffemakerService.class), mocha.addPcr(ent("user-id", "s18728")), result("recipes/s18728/mocha/added"));
		Routine createOrder = task(sig("createOrder", INextGenCoffemakerService.class), inVal("recipe", "recipes/s18728/mocha"), result("coffeemakers/hall-03/orders/s18728/01"));
		Routine terminateOrder = task(sig("terminateOrder", INextGenCoffemakerService.class), inVal("order", "coffeemakers/hall-03/orders/s18728/01"), result("coffeemakers/hall-03/orders/s18728/01/isTerminated"));

		Block terminateOrderBlock = block("orderCoffee", addMocha, createOrder, terminateOrder);
		Context result = context(exert(terminateOrderBlock));
		assertEquals(value(result, "coffeemakers/hall-03/orders/s18728/01/isTerminated"), true);
	}

    @Test
    public void orderCoffee() throws Exception {
        Routine addMocha = task(sig("addRecipe", INextGenCoffemakerService.class), mocha.addPcr(ent("user-id", "s18728")), result("recipes/s18728/mocha/added"));
        Routine createOrder = task(sig("createOrder", INextGenCoffemakerService.class), inVal("recipe", "recipes/s18728/mocha"), result("coffeemakers/hall-03/orders/s18728/01"));
        Routine checkInventory = task(sig("checkInventory", IInventoryService.class), inVal("order", "coffeemakers/hall-03/orders/s18728/01"), result("coffeemakers/hall-03/orders/s18728/01/isEnough"));
        Routine confirmPayment = task(sig("confirmPayment", IPaymentService.class), inVal("order", "coffeemakers/hall-03/orders/s18728/01"), result("coffeemakers/hall-03/orders/s18728/01/isPayedFor"));
        Routine makeCoffee = task(sig("makeCoffee", INextGenCoffemakerService.class), inVal("recipe", "recipes/s18728/mocha"), coffeemaker, result("coffeemakers/hall-03/orders/s18728/01/isMade"));
        Routine storeCoffee = task(sig("storeCoffee", INextGenCoffemakerService.class), inVal("order", "coffeemakers/hall-03/orders/s18728/01"), result("coffeemakers/hall-03/orders/s18728/01/isStored"));
        Routine giveCode = task(sig("giveCode", IAuthenticationService.class), inVal("order", "coffeemakers/hall-03/orders/s18728/01"), outPaths("coffeemakers/hall-03/orders/s18728/01/isCodeGiven", "coffeemakers/hall-03/orders/s18728/01/code"));
        Routine checkCode = task(sig("checkCode", IAuthenticationService.class), inVal("order", "coffeemakers/hall-03/orders/s18728/01"), inVal("code", "coffeemakers/hall-03/orders/s18728/01/code"), result("coffeemakers/hall-03/orders/s18728/01/isCodeValid"));
        Routine giveCoffee = task(sig("giveCoffee", INextGenCoffemakerService.class), inVal("order", "coffeemakers/hall-03/orders/s18728/01"), result("coffeemakers/hall-03/orders/s18728/01/isGiven"));

        Routine terminateOrder = task(sig("terminateOrder", INextGenCoffemakerService.class), inVal("order", "coffeemakers/hall-03/orders/s18728/01"), result("coffeemakers/hall-03/orders/s18728/01/isTerminated"));


        Block codeCheckGiveCoffee = block("inventoryCheckBlock", checkCode,
                alt(opt(condition((Context<Boolean> cxt) ->
                                value(cxt, "coffeemakers/hall-03/orders/s18728/01/isCodeValid")), giveCoffee),
                        opt(condition((Context<Boolean> cxt)
                                -> !value(cxt, "coffeemakers/hall-03/orders/s18728/01/isCodeValid")), terminateOrder)));

        Block paymentCheckMakeCoffee = block("inventoryCheckBlock", confirmPayment,
                alt(opt(condition((Context<Boolean> cxt) ->
                                value(cxt, "coffeemakers/hall-03/orders/s18728/01/isPayedFor")), makeCoffee),
                        opt(condition((Context<Boolean> cxt)
                                -> !value(cxt, "coffeemakers/hall-03/orders/s18728/01/isPayedFor")), terminateOrder)));

        Block inventoryPaymentCheckAndMakeCoffee = block("inventoryCheckBlock", checkInventory,
                alt(opt(condition((Context<Boolean> cxt) ->
                                value(cxt, "coffeemakers/hall-03/orders/s18728/01/isEnough")), paymentCheckMakeCoffee),
                        opt(condition((Context<Boolean> cxt)
                                -> !value(cxt, "coffeemakers/hall-03/orders/s18728/01/isEnough")), terminateOrder)));


        Block orderCoffee = block("orderCoffee", addMocha, createOrder, inventoryPaymentCheckAndMakeCoffee, storeCoffee, giveCode, codeCheckGiveCoffee, context(ent("drinkerBalance", 150)));
		Context result = context(exert(orderCoffee));
		assertEquals(value(result, "drinkerBalance"), 50.00);
        assertEquals(value(coffeemaker.getChild("inventory").getContext("coffeeAmt")), 42);
        assertEquals(value(coffeemaker.getChild("inventory").getContext("milkAmt")), 49);
        assertEquals(value(coffeemaker.getChild("inventory").getContext("sugarAmt")), 49);
        assertEquals(value(coffeemaker.getChild("inventory").getContext("chocolateAmt")), 48);

    }
}

