package nl.quintor.staq.java10.localvar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntUnaryOperator;

@SuppressWarnings("ALL")
public class LocalVar {

    public void varWithoutInitializer() {
        // Uncomment. Does it compile?
        //var myVariable;
        //myVariable = 2;
    }

    public void varWithGenerics() {
        // Replace type with var
        List<String> myList = new ArrayList<String>();
        myList.add("Hello World");
        // Uncomment. Does it compile? Why or why not? What is the type of "myList"?
        //myList.add(2);
    }

    public void varWithDiamond() {
        // Replace type with var
        List<String> myList = new ArrayList<>();
        myList.add("Hello World");
        // Uncomment. Does it compile? Why or why not?
        //myList.add(2);
    }

    public void varInForLoop() {
        var list = Arrays.asList(1, 2, 3);
        for (var number : list) {
            // What is the type of `number`? Is using reference equality safe here?
        }
    }

    public void arrayInitialization() {
        // Replace type with var. Does it compile?
        String[] array2 = { "a", "b", "c" };
    }

    public void explicitArrayInitialization() {
        // Replace type with var. Does it compile?
        String[] array1 = new String[] { "a", "b", "c" };
    }

    public void varReassignment() {
        // Replace tpye with var. Does it compile?
        int myNumber = 3;
        myNumber = 6;
        myNumber++;
    }

    public void varIsNotJavascript() {
        var myVariable = "Hello";
        // Uncomment. Does it compile? Why or why not?
        //myVariable = 123;
    }

    public void initializeWithVar() {
        var myList = new ArrayList<String>();
        // Uncomment. Does it compile?
        //var sameList = myList;
    }

    public void finalVar() {
        final var myValue = "Hello";
        // Uncomment. Does it compile?
        //myValue = "World";
    }

    public void varLambda() {
        // Uncomment. Does it compile?
        //var addOne = x -> x + 1;
    }

    public void varMethodReference() {
        // Uncomment. Does it compile?
        //var toString = Object::toString;
    }

    public void varLambdaWithCast() {
        // Uncomment. Does it compile?
        //var addOne = (IntUnaryOperator) x -> x + 1;
    }

    public void varInLambda() {
        DoubleBinaryOperator pythagoras = (a, b) -> {
            // Replace both types with var. Does it compile?
            double aSquared = a * a;
            double bSquared = b * b;
            return Math.sqrt(aSquared + bSquared);
        };
    }

    public void varArgument(
            // Uncomment. Does it compile?
            //var myArgument
    ) {
    }

    // Uncomment. Does it compile?
    //public var returnVar() {
    //     return "Hello";
    //}

    public void varLambdaArguments() {
        //IntUnaryOperator addOne = (var x) -> x + 1;
    }

    // Rename this class to "var"
    static class someclass {
    }

    public void varIsNotAClass() {
        //Class cls = var.class;
    }

    public void varIsNotAKeyword() {
        // 1. Uncomment. Does it compile?
        // 2. Replace "String" with "var". Keep the variable name the same. Does it compile?
        //String var = "someString";
    }

    public void voidMethod() {
        // Uncomment. Does it compile?
        //var resultOfVoidMethod = voidMethod();
    }

    static class VarMember {
        // Uncomment. Does it compile?
        //var memberVariable = "Hello";
    }

    static class Var {
        Var var;

        Var var() {
            // What is the type of the local variable "var"
            var var = this.var;
            // What is the type of var.var?
            var.var = var();
            return var;
        }
    }

    public void anonymousClass() {
        Object anonymousClass = new Object() {
            void myMethod() {
            }
        };
        // Is myMethod reachable from `anonymousClass`? Uncomment.
        // anonymousClass.myMethod();

        var anonymousVar = new Object() {
            void myMethod() {
            }
        };
        // Is myMethod reachable from `anonymousVar`? Uncomment.
        //anonymousVar.myMethod();
    }

    private interface Animal {
        String getAnimalName();
    }

    private interface CanQuack {
        default String quack() {
            return "Quack!";
        }
    }

    private interface CanWaggle {
        default String waggle() {
            return "Waggle waggle";
        }
    }

    public void intersectionType() {
        var duck = (Animal & CanWaggle & CanQuack) () -> "Duck";
        // What is the type of "duck"?

        // Uncomment. Does it compile? What does this return?
        //duck.getAnimalName();
        // Uncomment. Does it compile? What does this return?
        //duck.quack();
        // Uncomment. Does it compile? What does this return?
        //duck.waggle();
    }
}
