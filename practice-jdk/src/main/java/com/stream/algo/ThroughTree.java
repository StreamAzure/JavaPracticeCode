package com.stream.algo;

import com.stream.algo.common.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 二叉树迭代法，前序、中序、后序遍历
 */
public class ThroughTree {

    public static List<Integer> preorder(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        List<Integer> ans = new ArrayList<>();
        if (root == null) return ans;
        TreeNode p = root;
        while (p != null || !stack.isEmpty()) {
            if (p != null) {
                ans.add(p.val);
                stack.push(p);
                p = p.left;
            } else {
                TreeNode tmp = stack.pop();
                p = tmp.right;
            }
        }
        return ans;
    }

    public static List<Integer> inorder(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        List<Integer> ans = new ArrayList<>();
        if (root == null) return ans;
        TreeNode p = root;
        while (p != null || !stack.isEmpty()) {
            if (p != null) {
                stack.push(p);
                p = p.left;
            } else {
                TreeNode tmp = stack.pop();
                ans.add(tmp.val);
                p = tmp.right;
            }
        }
        return ans;
    }

    public static List<Integer> postorder(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        List<Integer> ans = new ArrayList<>();
        if (root == null) return ans;
        TreeNode p = root, pre = null;
        while (p != null || !stack.isEmpty()) {
            if (p != null) {
                stack.push(p);
                p = p.left;
            } else {
                TreeNode tmp = stack.pop();
                if (tmp.right == null || tmp.right == pre) {
                    ans.add(tmp.val);
                    pre = tmp;
                } else {
                    stack.push(tmp);
                    p = tmp.right;
                }
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        TreeNode root = BuildTree.buildTree(new int[]{1, 2, 3}, new int[]{1, 3, 2});
        System.out.println("前序遍历: " + preorder(root));
        System.out.println("中序遍历: " + inorder(root));
        System.out.println("后序遍历: " + postorder(root));
    }
}
