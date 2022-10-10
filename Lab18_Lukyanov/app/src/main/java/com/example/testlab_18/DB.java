package com.example.testlab_18;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DB extends SQLiteOpenHelper {
    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Graph (number INT, name TEXT);";
        db.execSQL(sql);
        sql = "CREATE TABLE Node (graph INT, number INT, x REAL, y REAL, text TEXT);";
        db.execSQL(sql);
        sql = "CREATE TABLE Link (graph INT, number INT, a INT, b INT, value REAL);";
        db.execSQL(sql);
    }

    public void onSaveGraph(Graph graph)
    {
        SQLiteDatabase db = getWritableDatabase();
        int graphID = getMaxId("Graph");
        graphID++;
        graph.number = graphID;
        int nodeID = getMaxId("Node");
        nodeID++;
        int linkID = getMaxId("Link");
        linkID++;
        String sql;
        sql = "INSERT INTO Graph VALUES(" + graphID + ", '" + graph.name + "');";
        db.execSQL(sql);
        for (int i = 0; i < graph.node.size(); i++)
        {
            sql = "INSERT INTO Node VALUES (" + graphID + ", " + nodeID + ", " + graph.node.get(i).x + ", " + graph.node.get(i).y + ", '" + graph.node.get(i).text + "');";
            db.execSQL(sql);
            nodeID++;
        }
        for (int i = 0; i < graph.link.size(); i++)
        {
            sql = "INSERT INTO Link VALUES (" + graphID + ", " + linkID + ", " + graph.link.get(i).a + ", " + graph.link.get(i).b + ", " + graph.link.get(i).value + ");";
            db.execSQL(sql);
            linkID++;
        }
    }

    public void onClearGraphs()
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM Graph;";
        db.execSQL(sql);
        sql = "DELETE FROM Node;";
        db.execSQL(sql);
        sql = "DELETE FROM Link;";
        db.execSQL(sql);
    }

    public int getMaxId(String table)
    {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT MAX(number) from " + table;
        Cursor cur = db.rawQuery(sql, null);
        if (cur.moveToFirst()) return cur.getInt(0);
        return -1;
    }

    public void onRenameGraph(String name, int number)
    {
        if (number < 0) return;
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE Graph SET name = '" + name + "' WHERE number = " + number + ";";
        db.execSQL(sql);
    }

    public void onDeleteGraph(int number)
    {
        if (number < 0) return;
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM Graph WHERE number = " + number + ";";
        db.execSQL(sql);
        sql = "DELETE FROM Node WHERE graph = " + number + ";";
        db.execSQL(sql);
        sql = "DELETE FROM Link WHERE graph = " + number + ";";
        db.execSQL(sql);
    }

    public void getAllGraphs(ArrayList<Graph> lst)
    {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM Graph;";
        Cursor cur = db.rawQuery(sql, null);
        Cursor curGraph;
        if (cur.moveToFirst())
        {
            do {
                Graph graph = new Graph();
                graph.number = cur.getInt(0);
                graph.name = cur.getString(1);
                sql = "SELECT * FROM Node WHERE graph = " + graph.number + ";";
                curGraph = db.rawQuery(sql, null);
                if (curGraph.moveToFirst())
                {
                    do {
                        graph.add_node(curGraph.getFloat(2), curGraph.getFloat(3));
                        int ab = curGraph.getPosition();
                        graph.node.get(ab).text = curGraph.getString(4);
                    }
                    while (curGraph.moveToNext());
                }
                sql = "SELECT * FROM Link WHERE graph = " + graph.number + ";";
                curGraph = db.rawQuery(sql, null);
                if (curGraph.moveToFirst())
                {
                    do {
                        graph.add_link(curGraph.getInt(2), curGraph.getInt(3), curGraph.getFloat(4));
                    }
                    while (curGraph.moveToNext());
                }
                lst.add(graph);
            }
            while (cur.moveToNext());
        }
    }


    public Graph onLoadGraph(int graphID)
    {
        SQLiteDatabase db = getReadableDatabase();
        Graph graph = new Graph();
        String sql = "SELECT * FROM Graph WHERE number = " + graphID + ";";
        Cursor cur = db.rawQuery(sql, null);
        Cursor curGraph;
        if (cur.moveToFirst())
        {
            graph.name = cur.getString(1);
            sql = "SELECT * FROM Node WHERE graph = " + graphID + ";";
            curGraph = db.rawQuery(sql, null);
            if (curGraph.moveToFirst())
            {
                do {
                    graph.add_node(curGraph.getFloat(2), curGraph.getFloat(3));
                    graph.node.get(curGraph.getPosition()).text = curGraph.getString(4);
                }
                while (curGraph.moveToNext());
            }
            sql = "SELECT * FROM Link WHERE graph = " + graphID + ";";
            curGraph = db.rawQuery(sql, null);
            if (curGraph.moveToFirst())
            {
                do {
                    graph.add_link(curGraph.getInt(2), curGraph.getInt(3), curGraph.getFloat(4));
                }
                while (curGraph.moveToNext());
            }
        }
        return graph;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
