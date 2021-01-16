package de.marvinleiers.potionmatheffects.utils;

import de.marvinleiers.potionmatheffects.PotionMathEffects;

import java.text.DecimalFormat;
import java.util.Random;

public class MathUtils
{
    private static Random random = new Random();

    public static String generateProblem()
    {
        int range = 15 * ((PotionMathEffects.getCustomConfig().getInt("difficulty") / 2) + 1);

        boolean withX = (random.nextInt(5) + 1) <= PotionMathEffects.getCustomConfig().getInt("difficulty");

        if (withX)
        {
            String operation = random.nextInt(2) == 0 ? "-" : "+";
            int first = random.nextInt(range - 5) + 1;
            int second = random.nextInt(range - 5) + 1;

            String problem = random.nextInt(2) == 0 ? "x " + operation + " " + new DecimalFormat("##.##").format(first) + " = " + new DecimalFormat("##.##").format(second) :
                    new DecimalFormat("##.##").format(first) + " " + operation + " x = " + new DecimalFormat("##.##").format(second);
            int solution = solve(problem);

            PotionMathEffects.getInstance().setCurrentSolution(solution);

            return problem;
        }
        else
        {
            String operation = "+";

            int x = random.nextInt(4);

            if (x == 1)
                operation = "-";
            else if (x == 2)
                operation = "/";
            else if (x == 3)
                operation = "*";

            double first = random.nextInt(range) + 1;
            double second = random.nextInt(range) + 1;

            String problem = new DecimalFormat("##.##").format(first) + " " + operation + " " + new DecimalFormat("##.##").format(second);
            double solution = first + second;

            switch (operation)
            {
                case "-":
                    solution = first - second;
                    break;
                case "/":
                    solution = first / second;
                    break;
                case "*":
                    solution = first * second;
                    break;
            }

            PotionMathEffects.getInstance().setCurrentSolution(solution);

            return problem + " = ?";
        }
    }

    private static int solve(String problem)
    {
        int right = Integer.parseInt(problem.split("=")[1].trim());
        String left = problem.split("=")[0].trim();
        String operation = left.split(" ")[1].trim();
        int number = 0;

        boolean numOnTheRight = false;

        if (operation.equals("+"))
        {
            try
            {
                number = Integer.parseInt(left.split("\\+")[0].trim());
            }
            catch (NumberFormatException e)
            {
                number = Integer.parseInt(left.split("\\+")[1].trim());
            }
        }
        else
        {
            try
            {
                number = Integer.parseInt(left.split("-")[0].trim());
            }
            catch (NumberFormatException e)
            {
                numOnTheRight = true;
                number = Integer.parseInt(left.split("-")[1].trim());
            }
        }

        int solution = -1;

        if (operation.equals("-"))
        {
            // 3 - x = 12
            // x - 21 = 6

            if (numOnTheRight)
            {
                solution = number + right;
            }
            else
            {
                solution = number - right;
            }
        }
        else
        {
            solution = right - number;
        }

        return solution;
    }
}
