# Distributed Implementation of Community Detection Algorithm

The objective is to efficiently calculate edge betweenness centrality and optimize modularity to do community detection.

This is a distributed implementation of popular Girvan-Newman algorithm for community detection. The algorithm and paper have been linked [here](https://arxiv.org/pdf/cond-mat/0112110.pdf).

Logic to calculate all-pair-shortest-path [https://gist.github.com/srirambaskaran/573927ee01f3673d3a9182bacbc9ed39](https://gist.github.com/srirambaskaran/573927ee01f3673d3a9182bacbc9ed39)


## Chanllenges
### Algorithm

1. Running all pair shortest path algorithm. Efficiently pass short messages across different partitions in Spark.
1. Computing graph measures: Betweenness-centrality

### Technical
1. Visualizing Large graphs (major constraint too).
1. Querying graph, providing insights.
1. Implementing a simple collaborative-filtering recommender system.

## Proposed Architecture

S3 -> Spark + GraphX -> TitanDB (backed by Cassandra, ElasticSearch) -> HTML + D3






## Dataset

[Movielens dataset](https://grouplens.org/datasets/movielens/20m/) - 20M user ratings

  Identifying communities among users with an objective to find users with similar interest.
  Building a rating based graph where an edge is created based on the 
  
[DBPedia](http://wiki.dbpedia.org/develop/datasets/dbpedia-version-2016-10) - 6.6M entities (nodes) - Really Huge!
 
Debuggung and testing:

[Movielens dataset(small)](https://grouplens.org/datasets/movielens/latest/)


### Abstract from the paper: 

> A number of recent studies have focused on the statistical properties of networked systems such
as social networks and the World-Wide Web. Researchers have concentrated particularly on a
few properties which seem to be common to many networks: **the small-world property, power-law
degree distributions, and network transitivity**. In this paper, we highlight another property which is
found in many networks, the property of community structure, in which **network nodes are joined
together in tightly-knit groups between which there are only looser connections**.

## Possible Usecases

- (Movie Lens) Identify tight knit groups in the network, provide customized services to them.
- (Movie Lens) Provide recommendations for movies based on the community structures.
- (Movie Lens) Identify evolving communities. Influencers in communities who have high **note-betweenness centrality**.
- (DB Pedia) Optimize search on a topic by focussing on the community of related documents.

### Edge-Betweenness Centrality

It is defined by the **_number of shortest paths_** between any two nodes that go through a given edge. This applies to both directed and undirected graphs.

Calculating number of shortest paths requirs computing **all pair shortest paths** for a given graph. Asymptotic 

### Modulartiy

Wikipedia:

> Modularity is one **measure of the structure** of networks or graphs. It was designed to measure the **strength of division** of a network into modules (also called groups, clusters or communities). Networks with high modularity have **dense connections between the nodes within modules but sparse connections between nodes in different modules**. Modularity is often used in **optimization methods for detecting community structure in networks**. However, it has been shown that modularity suffers a resolution limit and, therefore, it is unable to detect small communities. Biological networks, including animal brains, exhibit a high degree of modularity.

## Rough Algorithm

Step 1: Identify **edge-betweenness centrality** for each edge. This requires implemenation of an efficient all pair shortest path algorithm.

Step 2: Delete the edge with the **maximum** betweenness centrality. This edge will ideally be loosely connecting two heavily connected groups. (Be sure to keep the list of deleted edges in order.)

Step 3: Calculate **modularity** for the graph. It is the measure of cohesiveness of the graph, 

Step 4: Repeat Step 1-4 until the graph is completely disconnected.

Step 5: Identify "which edge deletion" resulted in highest modularity, add all the edges back till that step. 

Step 6: Return the connected graph as communities.

## What types of graphs will it work on?

This algorithm works for particular types of network where we can expect a tight knit group of nodes _interacting_ within the group and not outside the group. We can see similar behavior in social network graphs and people's interests in things.

## What types of graphs won't it work on?

Graphs following simple models like Erdos-Renyi where presence of an edge has equal probability across the entire network. You can find more details [here](https://en.wikipedia.org/wiki/Erd%C5%91s%E2%80%93R%C3%A9nyi_model).

## Technologies
- Spark & GraphX extension (with Pregel API).
- Cassandra for persistence communities info.
- TitanDB/Neo4j for persisting graph.
- D3 for visualization (Charts + Graph)
- HTML + JQuery + Ajax (Lazy Loading)

