package com.example.testlab_18;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {

    ListView lstctl;
    ArrayList<Graph> lst = new ArrayList<>();
    ArrayAdapter<Graph> adp;

    TextView tvSelectedGraph;
    EditText txtName;

    GraphView gv;
    Graph graph;

    Intent i;

    int selectedGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        g.graph = new DB(this, "graph.db", null, 1);

        tvSelectedGraph = findViewById(R.id.tvSelGraph);
        txtName = findViewById(R.id.editTextRename);
        lstctl = findViewById(R.id.listView);
        lstctl.setOnItemClickListener((parent, view, position, id) ->
        {
            selectedGraph = (int) id + 1;
            tvSelectedGraph.setText("Selected graph: " + selectedGraph);
        });
        adp = new ArrayAdapter<Graph>(this, android.R.layout.simple_list_item_1, lst);
        lstctl.setAdapter(adp);

        updateList();

        i = getIntent();
        graph = new Graph();
        int countNode, countLink;
        String text;
        int a, b;
        float x, y, value;
        countNode = i.getIntExtra("countNode", -1);
        countLink = i.getIntExtra("countLink", -1);
        for (int j = 0; j < countNode; j++)
        {
            x = i.getFloatExtra("graph_node_" + j + "x", -1);
            y = i.getFloatExtra("graph_node_" + j + "y", -1);
            text = i.getStringExtra("graph_node_" + j + "text");
            graph.add_node(x, y);
            graph.node.get(j).text = text;
        }
        for (int j = 0; j < countLink; j++)
        {
            a = i.getIntExtra("graph_link_" + j + "a", -1);
            b = i.getIntExtra("graph_link_" + j + "b", -1);
            value = i.getFloatExtra("graph_link_" + j + "value", -1);
            graph.add_link(a, b, value);
        }
    }

    public void updateList()
    {
        lst.clear();
        g.graph.getAllGraphs(lst);
        adp.notifyDataSetChanged();
    }

    public void onSave(View v)
    {
        graph.name = txtName.getText().toString();
        g.graph.onSaveGraph(graph);
        updateList();
    }

    public void onLoad(View v)
    {
        if (selectedGraph < 0) return;
        graph = g.graph.onLoadGraph(selectedGraph);
        int countNode = 0, countLink = 0;
        for (int j = 0; j < graph.node.size(); j++)
        {
            i.putExtra("graph_node_" + j + "x", graph.node.get(j).x);
            i.putExtra("graph_node_" + j + "y", graph.node.get(j).y);
            i.putExtra("graph_node_" + j + "text", graph.node.get(j).text);
            countNode++;
        }
        for (int j = 0; j < graph.link.size(); j++)
        {
            i.putExtra("graph_link_" + j + "a", graph.link.get(j).a);
            i.putExtra("graph_link_" + j + "b", graph.link.get(j).b);
            i.putExtra("graph_link_" + j + "value", graph.link.get(j).value);
            countLink++;
        }
        i.putExtra("countNode", countNode);
        i.putExtra("countLink", countLink);
        setResult(1000, i);
        finish();
    }

    public void onRename(View v)
    {
        g.graph.onRenameGraph(txtName.getText().toString(), selectedGraph);
        updateList();
    }

    public void onDelete(View v)
    {
        g.graph.onDeleteGraph(selectedGraph);
        updateList();
    }

    public void onCopy(View v)
    {
        g.graph.onSaveGraph(g.graph.onLoadGraph(selectedGraph));
        updateList();
    }

    public void onReset(View v)
    {
        g.graph.onClearGraphs();
        updateList();
    }


}