package com.hrqhrj.fc;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class ExampleUnitTest extends TestCase {

    public void testIn(){
        int[] nums1 = {1, 2, 2, 1};
        int[] nums2 = {2, 2};
        testIntersect(nums1, nums2);
    }

    /*
    执行用时 :
        8 ms, 在所有 Java 提交中击败了 62.31% 的用户
        内存消耗 :38.4 MB, 在所有 Java 提交中击败了 17.85% 的用户
    */
    void testIntersect(int[] nums1, int[] nums2) {
//        Map<Integer, Integer> map = new HashMap<>();
//        List<Integer> list = new ArrayList<>();
//        if (nums1.length > nums2.length) {
//            for (int num : nums2) {
//                if (map.containsKey(Integer.valueOf(num))) {/*true*/
//                    map.put(num, map.get(num).intValue() + 1);
//                } else {
//                    map.put(num, 1);
//                }
//            }
//            list.clear();
//            for (int num : nums1) {
//                if (map.containsKey(Integer.valueOf(num))) {
//                    int value = map.get(num).intValue() - 1;
//                    map.put(num, value);
//                    if (value == 0) {
//                        map.remove(num);
//                    }
//                    list.add(Integer.valueOf(num));
//                }
//            }
//        } else {
//            list.clear();
//            for (int num : nums1) {
//                if (map.containsKey(Integer.valueOf(num))) {/*true*/
//                    map.put(num, map.get(num).intValue() + 1);
//                } else {
//                    map.put(num, 1);
//                }
//            }
//
//            for (int num : nums2) {
//                if (map.containsKey(Integer.valueOf(num))) {
//                    int value = map.get(num).intValue() - 1;
//                    map.put(num, value);
//                    if (value == 0) {
//                        map.remove(num);
//                    }
//                    list.add(Integer.valueOf(num));
//                }
//            }
//        }
//        int[] ints = new int[list.size()];
//        int index = 0;
//        for (Integer integer : list) {
//            ints[index++] = integer.intValue();
//        }
//
//        return ints;


        List<Integer> list = new ArrayList<>();
        if (nums1.length > nums2.length) {
            for (int anInt : nums2) {
                list.add(Integer.valueOf(anInt));
            }
        } else {
            for (int anInt : nums1) {
                list.add(Integer.valueOf(anInt));
            }
        }
        List<Integer> nums = new ArrayList<>();
        if (nums1.length > nums2.length) {
            nums.clear();
            for (int i : nums1) {
                if (list.remove(Integer.valueOf(i))) {
                    nums.add(Integer.valueOf(i));
                }
            }
        } else {
            nums.clear();
            for (int i : nums2) {
                if (list.remove(Integer.valueOf(i))) {
                    nums.add(Integer.valueOf(i));
                }
            }
        }
        int[] ints = new int[nums.size()];
        int index = 0;
        for (Integer integer : nums) {
            ints[index++] = integer.intValue();
        }

//        return ints;
    }
}


