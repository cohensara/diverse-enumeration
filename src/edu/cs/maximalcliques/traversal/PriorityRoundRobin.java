package edu.cs.maximalcliques.traversal;

import java.util.*;


//PQ nodes have a score and a linked list of associated values
//When returning a top value, perform a round robin on the values in the linked list
public class PriorityRoundRobin<T> {

    static int id = 0;

    public static void reset() {
        id = 0;
    }

    //item ids to item node in score hashtable
    Hashtable<Integer, IdValues<T>> idsToValues = new Hashtable<>();

    //scores to ids with score

    Hashtable<Integer, MyList<Integer>> scoresToItems = new Hashtable<>();

    //scores of existing ids
    TreeSet<Integer> scores = new TreeSet<>();

    //all item ids
    MyList<Integer> allItems = null;

    public int numAllItems() {
        return allItems.size();
    }

    //public boolean isEmpty() {
    //   return allItems == null || allItems.size() == 0;
    //}

    public boolean isScoredItem(int id) {

        return idsToValues.get(id).nodeInScoreList != null;
    }

    public boolean isEmptyAllItems() {
        return allItems == null || allItems.size() == 0;
    }

    public boolean hasScoredItems() {
        return !scores.isEmpty();
    }

    public void add(int score, T val) {
        add(score, val, id);
        id++;
    }

    public void add(int score, T val, int id) {
        //System.out.println("add score=" + score);
        MyNode<Integer> allNode;
        if (allItems == null) {
            allNode = new MyNode<>(id);
            allItems = new MyList<>(allNode);
        }
        else {
            allNode = allItems.add(id);
        }
        MyNode<Integer> scoreNode = null;
        if (score != -1) {
            if (scores.add(score)) {
                scoreNode = new MyNode<>(id);
                MyList<Integer> list = new MyList(scoreNode);
                scoresToItems.put(score, list);
            } else {
                MyList<Integer> list = scoresToItems.get(score);
                scoreNode = list.add(id);
            }
        }
        idsToValues.put(id, new IdValues<T>(val, scoreNode, allNode, score));
    }

    public T idValue(int id) {
        IdValues idValues = idsToValues.get(id);
        if (idValues == null) {
            System.out.println("Missing id:" + id);
            if (!idsToValues.isEmpty()) System.out.println("idsToValues=" + idsToValues);
            if (allItems.size() > 0) System.out.println("allItems=" + allItems);
            if (!scores.isEmpty()) System.out.println("scores=" + scores);
            if (!scoresToItems.isEmpty()) System.out.println("scoresToItems=" + scoresToItems);
        }
        return idsToValues.get(id).val;
    }

    public int nextBestId() {

        if (scores.isEmpty()) {
            return -1;
        }
        int bestScore = scores.first();
        MyList<Integer> list = scoresToItems.get(bestScore);
        return list.next();
    }

    public int nextId() {
        return allItems.next();
    }

    public void updateScore(int id, int score) {

        IdValues<T> idValues = idsToValues.get(id);
        int oldScore = idValues.score;
        if (oldScore == score) {
            return;
        }

        if (oldScore != -1) {
            //remove from oldScore list
            MyList<Integer> list = scoresToItems.get(oldScore);
            list.remove(idValues.nodeInScoreList);
            if (list.size() == 0) {
                scoresToItems.remove(idValues.score);
                scores.remove(idValues.score);
            }
        }
        idValues.score = score;
        if (score == -1) {
            idValues.nodeInScoreList = null;
        }
        else {
            //add to score list
            MyNode<Integer> scoreNode = null;
            if (scores.add(score)) {
                scoreNode = new MyNode<>(id);
                MyList<Integer> list = new MyList(scoreNode);
                scoresToItems.put(score, list);
            }
            else {
                MyList<Integer> list = scoresToItems.get(score);
                scoreNode = list.add(id);
                list.next();
            }
            idValues.nodeInScoreList = scoreNode;
        }
    }

    public void removeId(int id) {
        //System.out.println("remove=" + id);
        IdValues<T> idValues = idsToValues.remove(id);
        allItems.remove(idValues.nodeInAllList);
        if (idValues.score != -1) {
            MyList<Integer> list = scoresToItems.get(idValues.score);
            list.remove(idValues.nodeInScoreList);
            if (list.size() == 0) {
                scoresToItems.remove(idValues.score);
                scores.remove(idValues.score);
                //System.out.println("remove score=" + idValues.score);
            }
        }
    }

}

class IdValues<T> {

    T val;
    MyNode<Integer> nodeInScoreList;
    MyNode<Integer> nodeInAllList;
    int score;

    public IdValues(T val, MyNode<Integer> scoreNode, MyNode<Integer> allNode, int score) {
        this.val = val;
        this.nodeInScoreList = scoreNode;
        this.nodeInAllList = allNode;
        this.score = score;
    }
}