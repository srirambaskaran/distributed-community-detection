import networkx as nx
import community as cd
import re
import sys
import time

# Single machine runs of louvain communtiy detection algorithms.
# Using implementation https://github.com/taynaud/python-louvainself.
# Using networkx to create graph.

# TODO may have to write a custom method for reading other ways the graphs are stored
def create_graph(filename):
    edges = []
    with open(filename) as f:

        for line in f:
            if line.startswith("#"):
                continue

            tokens = re.split("\\s+", line)
            from_node = int(tokens[0])
            to_node = int(tokens[1])

            edges.append((from_node, to_node))

        f.close()

        G=nx.Graph()
        G.add_edges_from(edges)
    return G



def main(args):

    filename = args[1]
    print("Running Community detection on file: "+filename)
    start = time.time()
    G = create_graph(filename)
    time_create = time.time()

    print("Create graph: "+str((time_create - start))+" seconds.")

    communities = cd.best_partition(G)
    time_compute = time.time()

    print("Compute communities: "+str((time_compute - time_create))+" seconds after create.")


if __name__ == "__main__":
    main(sys.argv)
