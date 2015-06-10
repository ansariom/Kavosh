package edu.lbb.kavosh.data.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utility {

	public static long getNumericValue(String s) {
		String newS = "";
		char[] sc = s.toCharArray();
		for (int i = 0; i < sc.length; i++) {
			if (Character.isDigit(sc[i])) {
				newS += sc[i];
			}
		}
		return Long.valueOf(newS).longValue();
	}

	public static int[] generatePermutationList(int motifSize) {
		List<Integer> permList = new ArrayList<Integer>();
		String permString = "";
		for (int i = 0; i < motifSize; i++) {
			permString += i;
		}
		permutation(permString, permList);
		int[] permutationList = new int[permList.size()];
		for (int i = 0; i < permList.size() / motifSize; i++) {
			for (int j = 0; j < motifSize; j++) {
				int index = i * motifSize + j;
				permutationList[index] = (permList.get(index));
			}
		}
//		for (int i = 0; i < permutationList.length; i++) {
//			System.out.print(permutationList[i]);
//		}
		return permutationList;

	}

	public static void permutation(String str, List<Integer> permList) {
		permutation("", str, permList);
	}

	private static void permutation(String prefix, String str,
			List<Integer> permList) {
		int n = str.length();
		if (n == 0) {
//			System.out.println(prefix);
			for (int i = 0; i < prefix.length(); i++) {
				permList.add(Integer.valueOf(prefix.substring(i, i + 1)));
			}
		} else {
			for (int i = 0; i < n; i++)
				permutation(prefix + str.charAt(i),
						str.substring(0, i) + str.substring(i + 1, n), permList);
		}
	}

	public static void main(String[] args) {
		generatePermutationList(4);
	}
}
