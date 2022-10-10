package com.example.testlab_18;

import java.util.ArrayList;

public class Graph
{
    public String name;
    public int number;

    public ArrayList <Node> node = new ArrayList <Node> ();
    public ArrayList <Link> link = new ArrayList <Link> ();

    public void add_node(float x, float y)
    {
        node.add(new Node(x, y, ""));
    }
    public void add_link(int a, int b, float value)
    {
        link.add(new Link(a, b, value));
    }

    public void remove_node(int index)
    {
        if (index < 0) return;
        node.remove(index);
    }

    public void remove_link(int index)
    {
        if (index < 0) return;
        link.remove(index);
    }

    public String toString()
    {
        return number + "\t|\t" + name + "\t|\tNodes: " + node.size() + "\t|\tLinks: " + link.size();
    }
}
