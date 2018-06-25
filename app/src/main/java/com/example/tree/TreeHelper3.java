package com.example.tree;

import com.example.yzcl.R;
import com.zhy.tree.bean.TreeNodeId;
import com.zhy.tree.bean.TreeNodeLabel;
import com.zhy.tree.bean.TreeNodePid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2018/1/19.
 */

public class TreeHelper3 {
    /**
     * 过滤出所有可见的Node
     *
     * @param nodes
     * @return
     */
    public static List<Node3> filterVisibleNode(List<Node3> nodes)
    {
        List<Node3> result = new ArrayList<Node3>();

        for (Node3 node : nodes)
        {
            // 如果为跟节点，或者上层目录为展开状态
            if (node.isRoot() || node.isParentExpand())
            {
                setNodeIcon(node);
                result.add(node);
            }
        }

        return result;
    }
    /**
     * 把一个节点上的所有的内容都挂上去
     *
     *
     */
    public static void addNode(List<Node3> nodes, Node3 node,
                               int defaultExpandLeval, int currentLevel)
    {
        nodes.add(node);
        if (defaultExpandLeval >= currentLevel)
        {
            node.setExpand(true);
        }

        if (node.isLeaf())
            return;
        for (int i = 0; i < node.getChildren().size(); i++)
        {
            addNode(nodes, node.getChildren().get(i), defaultExpandLeval,
                    currentLevel + 1);
        }
    }

    public static List<Node3> getRootNodes(List<Node3> nodes)
    {
        List<Node3> root = new ArrayList<Node3>();
        for (Node3 node : nodes)
        {
            if (node.isRoot())
                root.add(node);
        }
        return root;
    }

    /**
     * 将我们的数据转化为树的节点
     *
     * @param datas
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static <T> List<Node3> convetData2Node(List<T> datas)
            throws IllegalArgumentException, IllegalAccessException

    {
        List<Node3> nodes = new ArrayList<Node3>();
        Node3 node = null;

        for (T t : datas)
        {
            int id = -1;
            int pId = -1;
            String label = null;
            String useid=null;
            String customer=null;
            int aty=-1;
            int temQty=-1;
            Class<? extends Object> clazz = t.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field f : declaredFields)
            {
                if (f.getAnnotation(TreeNodeId.class) != null)
                {
                    f.setAccessible(true);
                    id = f.getInt(t);
                }
                if (f.getAnnotation(TreeNodePid.class) != null)
                {
                    f.setAccessible(true);
                    pId = f.getInt(t);
                }
                if (f.getAnnotation(TreeNodeLabel.class) != null)
                {
                    f.setAccessible(true);
                    label = (String) f.get(t);
                }
//                if (f.getAnnotation(TreeNodeLabel1.class) != null)
//                {
//                    f.setAccessible(true);
//                    aty = (int) f.get(t);
//                }
//                if (f.getAnnotation(TreeNodeLabel2.class) != null)
//                {
//                    f.setAccessible(true);
//                    temQty = (int) f.get(t);
//                }
                if (f.getAnnotation(TreeNodeUseid.class) != null)
                {
                    f.setAccessible(true);
                    useid= (String) f.get(t);
                }
                if (f.getAnnotation(TreeNodeCustomer.class) != null)
                {
                    f.setAccessible(true);
                    customer= (String) f.get(t);
                }
                if (id != -1 && pId != -1 && label != null&& useid!=null&&customer!=null)
                {
                    break;
                }
            }

            node = new Node3(id, pId, label,useid);
            nodes.add(node);
        }

        /**
         * 设置Node3间，父子关系;让每两个节点都比较一次，即可设置其中的关系
         */
        for (int i = 0; i < nodes.size(); i++)
        {
            Node3 n = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++)
            {
                Node3 m = nodes.get(j);
                if (m.getpId() == n.getId())
                {
                    n.getChildren().add(m);
                    m.setParent(n);
                } else if (m.getId() == n.getpId())
                {
                    m.getChildren().add(n);
                    n.setParent(m);
                }
            }
        }

        // 设置图片
        for (Node3 n : nodes)
        {
            setNodeIcon(n);
        }
        return nodes;
    }

    /**
     * 传入我们的普通bean，转化为我们排序后的Node3
     *
     * @param datas
     * @return
     * @throws IllegalArgumentException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static <T> List<Node3> getSortedNodes(List<T> datas,
                                                int defaultExpandLevel) throws IllegalArgumentException,
            IllegalAccessException

    {
        List<Node3> result = new ArrayList<Node3>();
        List<Node3> nodes = convetData2Node(datas);
        List<Node3> rootNodes = getRootNodes(nodes);
        for (Node3 node : rootNodes)
        {
            addNode(result, node, defaultExpandLevel, 1);
        }

        return result;
    }

    /**
     * 设置节点的图标
     *
     * @param node
     */
    public static void setNodeIcon(Node3 node)
    {
        if (node.getChildren().size() > 0 && node.isExpand())
        {
            node.setIcon(R.drawable.tree_ex);
        } else if (node.getChildren().size() > 0 && !node.isExpand())
        {
            node.setIcon(R.drawable.tree_ec);
        } else
            node.setIcon(-1);

    }
}
