~[close-circle](logo.png)

# Optimized computation of communities in large graphs

This project provides a distributed implementation of communitiy detection algorithm on large graphs. This implements the Louvain community detection algorithm in a distributed way by taking advantaged of the neighborhood agreggation strategy employed in the algorithm. 

This has been implemented using Spark and GraphX packages, using a simple data pipeline which can be easily extended to apply on any kinds of graphs. 

Contents
1. [Introduction]()
1. [Pipeline and Deployment Instructions]()
1. [Neighborhood Agreggation]()
1. [Graph Partitioning Strategies]()
1. [Performance metrics]()
1. [Datasets]()
1. [Future work]()


## Chanllenges
### Algorithm

1. Running all pair shortest path algorithm. Efficiently pass short messages across different partitions in Spark.
1. Computing graph measures: Betweenness-centrality

### Technical
1. Visualizing Large graphs (major constraint too).
1. Querying graph, providing insights.
1. Implementing a simple collaborative-filtering recommender system

## Proposed Architecture

S3 -> Spark + GraphX -> MySQL -> HTML + D3


### Abstract from the paper: 

> A number of recent studies have focused on the statistical properties of networked systems such
as social networks and the World-Wide Web. Researchers have concentrated particularly on a
few properties which seem to be common to many networks: **the small-world property, power-law
degree distributions, and network transitivity**. In this paper, we highlight another property which is
found in many networks, the property of community structure, in which **network nodes are joined
together in tightly-knit groups between which there are only looser connections**.

### Modulartiy

Wikipedia:

> Modularity is one **measure of the structure** of networks or graphs. It was designed to measure the **strength of division** of a network into modules (also called groups, clusters or communities). Networks with high modularity have **dense connections between the nodes within modules but sparse connections between nodes in different modules**. Modularity is often used in **optimization methods for detecting community structure in networks**. However, it has been shown that modularity suffers a resolution limit and, therefore, it is unable to detect small communities. Biological networks, including animal brains, exhibit a high degree of modularity.

## What types of graphs will it work on?

This algorithm works for particular types of network where we can expect a tight knit group of nodes _interacting_ within the group and not outside the group. We can see similar behavior in social network graphs and people's interests in things.

## What types of graphs won't it work on?

Graphs following simple models like Erdos-Renyi where presence of an edge has equal probability across the entire network. You can find more details [here](https://en.wikipedia.org/wiki/Erd%C5%91s%E2%80%93R%C3%A9nyi_model).

## Technologies
- S3 for storing initial graph.
- Spark & GraphX extension.
- MySQL for persisting graphs.
- D3 for visualization (Charts + Graph)
- HTML + JQuery + Ajax (Lazy Loading)


Demo -> bit.ly/close-circle

