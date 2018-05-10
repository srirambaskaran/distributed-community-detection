package org.insight.cd.algorithms

class VertedData(val community: Long,
                 val internalTotalEdgeWeight: Long,
                 val degree: Long,
                 val changed: Boolean) extends Serializable {

    override def toString = s"LouvainData($community, $internalTotalEdgeWeight, $degree, $changed)"
}