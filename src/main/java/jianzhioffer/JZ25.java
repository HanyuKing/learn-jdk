package jianzhioffer;

import java.util.ArrayList;
import java.util.List;

/**
 * 复杂链表的复制
 *
 * 输入一个复杂链表（每个节点中有节点值，以及两个指针，一个指向下一个节点，另一个特殊指针random指向一个随机节点），
 * 请对此链表进行深拷贝，并返回拷贝后的头结点。（注意，输出结果中请不要返回参数中的节点引用，否则判题程序会直接返回空）。
 * 下图是一个含有5个结点的复杂链表。图中实线箭头表示next指针，虚线箭头表示random指针。为简单起见，指向null的指针没有画出。
 */
public class JZ25 {
    public RandomListNode Clone(RandomListNode pHead) {
        if (pHead == null) {
            return null;
        }

        RandomListNode pHeadTemp = pHead;
        RandomListNode newHead = new RandomListNode(-1);
        RandomListNode newHeadTemp = newHead;

        List<RandomListNode> list = new ArrayList<>();

        while (pHeadTemp != null) {
            RandomListNode newNode = new RandomListNode(pHeadTemp.label);
            list.add(newNode);
            newHeadTemp.next = newNode;
            newHeadTemp = newHeadTemp.next;
            pHeadTemp = pHeadTemp.next;
        }

        pHeadTemp = pHead;

        for (RandomListNode node : list) {
            if (pHeadTemp.random != null) {
                node.random = new RandomListNode(pHeadTemp.random.label);
            }
            pHeadTemp = pHeadTemp.next;
        }

        return newHead.next;
    }


    class RandomListNode {
        int label;
        RandomListNode next = null;
        RandomListNode random = null;

        RandomListNode(int label) {
            this.label = label;
        }
    }
}
