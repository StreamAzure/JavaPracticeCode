package com.stream.algo;



public class BuildTree {
    public static class TreeNode {

        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(){}
        public TreeNode(int val) {
            this.val = val;
        }
    }

    public TreeNode build(int[] preorder, int pre_begin, int pre_end, int[] inorder, int in_begin, int in_end) {
        // 下标都是左闭右开
        if (pre_begin == pre_end) return null;
        if (in_begin == in_end) return null;

        TreeNode now = new TreeNode(preorder[pre_begin]);
        // 在中序遍历中找到根节点
        int target;
        for (target = in_begin; target < in_end ;target ++) {
            if (inorder[target] == preorder[pre_begin]) break;
        }
        // 得到左子树的节点数量
        int left_num = target - in_begin;
        // 得到右子树的节点数量
        int right_num = in_end - target - 1;
        now.left = build(preorder, pre_begin + 1, left_num + 1, inorder, in_begin, target);
        now.right = build(preorder, pre_begin + left_num + 1, pre_end, inorder, target + 1, in_end);

        return now;
    }
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        return build(preorder, 0, preorder.length, inorder, 0, inorder.length);
    }

    public static void main(String[] args) {
        BuildTree buildTree = new BuildTree();
        TreeNode root = buildTree.buildTree(new int[]{3,9,20,15,7}, new int[]{9,3,15,20,7});
        System.out.println(root.val);
    }
}
