# MST Edge Removal & Replacement

## Overview

This program constructs a **Minimum Spanning Tree (MST)** from a weighted graph using **Kruskal’s algorithm**.
After building the MST, it:

- **Randomly removes** one MST edge
- Detects the two components created by the removal
- Finds the **best replacement edge** (the lightest edge reconnecting the components)
- Builds the updated MST
- Generates **PNG images** showing each stage of the process

Removed edges are visualized in **red**, and replacement edges in **green**.

---

## How to Run

```
javac -d out $(find . -name "*.java")
java -cp out mst.app.Main
```

---

## Input

The graph is defined directly in the code.
No external input files are required.
You can freely modify the list of edges to test different graphs.

---

## Output

The program produces four image files:

- `1_original.png` — the full original graph
- `2_mst.png` — the minimum spanning tree
- `3_after_removal.png` — MST after removing a random edge
- `4_new_mst.png` — the updated MST with the replacement edge

A graphical window also displays the final MST.

---

## Random Removal & Best Replacement

- One MST edge is chosen **at random** each run.
- The program finds the **minimum-weight valid edge** that reconnects the separated components.
- This guarantees the resulting structure remains a correct MST.

---
