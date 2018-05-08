package org.insight.cd.algorithms

case class LouvainConfig(
                            inputFile: String,
                            outputDir: String,
                            minEdgeWeight: Int,
                            minimumCompressionProgress: Int,
                            progressCounter: Int,
                            delimiter: String)