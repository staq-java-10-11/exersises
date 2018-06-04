package nl.quintor.staq.java10.localvar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntUnaryOperator;

@SuppressWarnings("ALL")
public class LocalVar {

    public void varWithoutInitializer() {
        //var myVariable;
        //myVariable = 2;
    }

    public void varWithGenerics() {
        var myList = new ArrayList<String>();
        myList.add("Hello World");
        //myList.add(2);
    }

    public void varWithDiamond() {
        var myList = new ArrayList<>();
        myList.add("Hello World");
        myList.add(2);
        // Why does this compile?
    }

    public void varInForLoop() {
        var list = Arrays.asList(1, 2, 3);
        for (var number : list) {
            // What is the type of `number`? Is using reference equality safe here?
        }
    }

    public void varReassignment() {
        var myNumber = 3;
        myNumber = 6;
        myNumber++;
    }

    public void varIsNotJavascript() {
        var myVariable = "Hello";
        //myVariable = 123;
    }

    public void initializeWithVar() {
        var myList = new ArrayList<String>();
        var sameList = myList;
    }

    public void finalVar() {
        final var myValue = "Hello";
        //myValue = "World";
    }

    public void varLambda() {
        //var addOne = x -> x + 1;
    }

    public void varMethodReference() {
        //var toString = Object::toString;
    }

    public void varLambdaWithCast() {
        var addOne = (IntUnaryOperator) x -> x + 1;
    }

    public void varInLambda() {
        DoubleBinaryOperator pythagoras = (a, b) -> {
            var aSquared = a * a;
            var bSquared = b * b;
            return Math.sqrt(aSquared + bSquared);
        };
    }

    public void varArgument(
            //var myArgument
    ) {
    }

    //public var returnVar() {
    //     return "Hello";
    //}

    public void varLambdaArguments() {
        //IntUnaryOperator addOne = (var x) -> x + 1;
    }

    public void varIsNotAClass() {
        //Class cls = var.class;
    }

    static class VarMember {
        //var memberVariable = "Hello";
    }

    static class Var {
        Var var;

        Var var() {
            var var = this.var;
            var.var = var();
            return var;
        }
    }

    public void anonymousClass() {
        var anonymousVar = new Object() {
            void myMethod() {
                // Is this method reachable from `anonymousVar`?
            }
        };
        anonymousVar.myMethod();
    }

    private interface Animal {
        String getAnimalName();
    }

    private interface CanQuack {
        default void quack() {
            System.out.println("Quack!");
        }
    }

    private interface CanWaggle {
        default void waggle() {
            System.out.println("Waggle waggle");
        }
    }

    public void intersectionType() {
        var duck = (Animal & CanWaggle & CanQuack) () -> "Duck";
        duck.getAnimalName(); // returns "Duck"
        duck.quack(); // prints "Quack!"
        duck.waggle(); // prints "Waggle waggle"
    }
}
