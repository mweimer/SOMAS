/**
 * Utils.java
 * @author Antonio J. Nebro
 * @version 1.0
 * 
 * Description: utilities functions
 */
package jmetal.metaheuristics.moead;

/**
 *
 */
public class Utils {

  static double distVector(double[] vector1, double[] vector2) {
    int dim = vector1.length;
    double sum = 0;
    for (int n = 0; n < dim; n++) {
      sum += (vector1[n] - vector2[n]) * (vector1[n] - vector2[n]);
    }
    return Math.sqrt(sum);
  } // distVector

  static void minFastSort(double x[], int idx[], int n, int m) {
    for (int i = 0; i < m; i++) {
      for (int j = i + 1; j < n; j++) {
        if (x[i] > x[j]) {
          double temp = x[i];
          x[i] = x[j];
          x[j] = temp;
          int id = idx[i];
          idx[i] = idx[j];
          idx[j] = id;
        } // if
      }
    } // for

  } // minFastSort

  static void randomPermutation(int[] perm, int size) {
    int[] index = new int[size];
    boolean[] flag = new boolean[size];

    for (int n = 0; n < size; n++) {
      index[n] = n;
      flag[n] = true;
    }

    int num = 0;
    while (num < size) {
      int start = jmetal.util.PseudoRandom.randInt(0, size - 1);
      //int start = int(size*nd_uni(&rnd_uni_init));
      while (true) {
        if (flag[start]) {
          perm[num] = index[start];
          flag[start] = false;
          num++;
          break;
        }
        if (start == (size - 1)) {
          start = 0;
        } else {
          start++;
        }
      }
    } // while
  } // randomPermutation
}
