public class Main {

    private static Point point = new Point(0, 0);
    private static Vector vector = new Vector();

    public static void main(String[] args) {

        double eps = 0.000000001;

        double step = 0.2;

        Point[] array = new Point[10];

        Optimization.gradientСhangeableStep(array, point, vector, eps);
        System.out.println(Optimization.count + " " + Function.counter_func);
        System.out.format("Minimum of the function = %.8f at the point = (%.8f, %.8f)%n", point.value, point.x1, point.x2);
        /*point.x1 = 0;
        point.x2 = 0;
        Optimization.gradientConstantStep(point, vector, eps, step);
        System.out.format("Minimum of the function = %.8f at the point = (%.8f, %.8f)%n", point.value, point.x1, point.x2);*/
    }
}

class Optimization {

    static int count = 0;

    static void check_for_ort(Point[] array, int counter) {

        double result = 100;
        if (counter > 2) {
            double vector11 = array[counter].x1 - array[counter - 1].x1;
            double vector12 = array[counter].x2 - array[counter - 1].x2;

            double vector21 = array[counter - 1].x1 - array[counter - 2].x1;
            double vector22 = array[counter - 1].x2 - array[counter - 2].x2;

            result = vector11 * vector21 + vector12 * vector22;
        }
        System.out.println("ORT = "+ result);
    }

    static double leftEdgeOfInterval(Point point, Vector vector) {

        double step = 0.001;
        double prevStep = 0;

        int i = 0;
        while (Function.func(point, vector, step) <= Function.func(point, vector, prevStep)) {
            prevStep = step;
            step += 0.1;
            i++;
        }
        System.out.println();

        return step;

    }

    static void gradientСhangeableStep(Point[] array, Point point, Vector vector, double eps) {


        Function.gradient(point, vector);

        double a = 0;
        double b = leftEdgeOfInterval(point, vector);

        PointX minPoint = new PointX(a, a, b, Function.func(point, vector, a));

        PointX y = new PointX(a, Function.func(point, vector, a));
        PointX z = new PointX(b, Function.func(point, vector, b));

        double step = dihot(point, vector, minPoint, y, z, eps, eps / 100);

        System.out.println("Step = " + step);

        point.x1 = point.x1 - step * vector.valueX;
        point.x2 = point.x2 - step * vector.valueY;

        array[count] = point;

        check_for_ort(array, count);

        Function.func(point, vector, 0);
        Function.gradient(point, vector);

        if (Function.checkForOptimum(point, vector, eps)) {
            return;
        }
        count++;
        gradientСhangeableStep(array, point, vector, eps);

    }

    private static double dihot(Point point, Vector vector, PointX minPoint, PointX y, PointX z, double eps, double a) {

        double length;

        y.x = (minPoint.previous + minPoint.next - a) / 2;
        y.value = Function.func(point, vector, y.x);

        z.x = (minPoint.previous + minPoint.next + a) / 2;
        z.value = Function.func(point, vector, z.x);

        if (y.value <= z.value)
            minPoint.next = z.x;
        else
            minPoint.previous = y.x;

        length = Math.abs(minPoint.next - minPoint.previous);

        if (length < eps) {
            minPoint.x = (minPoint.next + minPoint.previous) / 2;
            minPoint.value = Function.func(point, vector, minPoint.x);
            return minPoint.x;
        }

        dihot(point, vector, minPoint, y, z, eps, a);
        return minPoint.x;
    }

    static void gradientConstantStep(Point point, Vector vector, double eps, double step) {

        Function.gradient(point, vector);


        point.x1 = point.x1 - step * vector.valueX;
        point.x2 = point.x2 - step * vector.valueY;


        //Function.leftEdgeOfInterval(point,vector);
        Function.func(point, vector, 0);
        Function.gradient(point, vector);


        if (Function.checkForOptimum(point, vector, eps)) {
            System.out.println(count);
            count = 0;
            return;
        }
        count++;
        gradientConstantStep(point, vector, eps, step);


    }

}

class Function {

    static int counter_func = 0;

    static double func(Point point, Vector vector, double step) {

        counter_func++;
        point.value = Math.pow(point.x1 - step * vector.valueX, 2) + 2 * Math.pow(point.x2 - step * vector.valueY, 2) - (point.x1 - step * vector.valueX) + 2 * (point.x2 - step * vector.valueY) + Math.exp(Math.pow(point.x1 - step * vector.valueX, 2) + Math.pow(point.x2 - step * vector.valueY, 2));
        return point.value;
    }

    static void gradient(Point point, Vector vector) {
        vector.valueX = 2 * point.x1 - 1 + 2 * point.x1 * Math.exp(Math.pow(point.x1, 2) + Math.pow(point.x2, 2));
        vector.valueY = 4 * point.x2 + 2 + 2 * point.x2 * Math.exp(Math.pow(point.x1, 2) + Math.pow(point.x2, 2));
    }

    static double sqrNorm(Point point, Vector vector) {
        return Math.pow(vector.valueX, 2) + Math.pow(vector.valueY, 2);
    }

    static boolean checkForOptimum(Point point, Vector vector, double eps) {
        return sqrNorm(point, vector) < eps ? true : false;
    }

}

class PointX {
    double previous;
    double next;
    double x;
    double value;

    PointX(double x, double value) {
        this.x = x;
        this.value = value;
    }

    PointX(double previous, double x, double next, double value) {
        this.x = x;
        this.next = next;
        this.previous = previous;
        this.value = value;
    }

}

class Point {

    double previous;
    double next;
    double x1;
    double x2;
    double value;


    Point(double x1, double x2) {
        this.x1 = x1;
        this.x2 = x2;
    }

    Point(double x1, double x2, double value) {
        this.x1 = x1;
        this.x2 = x2;
        this.value = value;
    }

}

class Vector {
    double valueX;
    double valueY;
}

