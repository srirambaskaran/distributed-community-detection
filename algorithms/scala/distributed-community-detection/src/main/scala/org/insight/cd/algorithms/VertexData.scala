package org.insight.cd.algorithms

class VertexData(val community: Long,
                 val internalTotalEdgeWeight: Long,
                 val degree: Long,
                 val changed: Boolean) extends Serializable {

    override def toString = s"VertexData($community, $internalTotalEdgeWeight, $degree, $changed)"
}