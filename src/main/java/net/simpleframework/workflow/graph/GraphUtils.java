package net.simpleframework.workflow.graph;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxGraph;

import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;
import net.simpleframework.workflow.schema.AbstractTaskNode;
import net.simpleframework.workflow.schema.EndNode;
import net.simpleframework.workflow.schema.Node;
import net.simpleframework.workflow.schema.ProcessDocument;
import net.simpleframework.workflow.schema.StartNode;
import net.simpleframework.workflow.schema.TransitionNode;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class GraphUtils {

	public static mxGraph createGraph(final ProcessDocument doc) {
		final mxGraph graph = new mxGraph();
		graph.setResetEdgesOnConnect(false);

		// 装载缺省的style
		final Map<String, Object> style = graph.getStylesheet().getDefaultEdgeStyle();
		style.put(mxConstants.STYLE_EDGE, mxEdgeStyle.EntityRelation);
		style.put(mxConstants.STYLE_ROUNDED, true);
		style.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_OPEN);

		final Map<String, TaskCell> taskCells = new HashMap<String, TaskCell>();
		final Collection<Node> nodes = doc.getProcessNode().nodes();
		for (final Node node : nodes) {
			if (node instanceof AbstractTaskNode) {
				double x = node.getDoubleProperty("x");
				double y = node.getDoubleProperty("y");
				if (node instanceof StartNode) {
					x = x == 0 ? 20 : x;
					y = y == 0 ? 200 : y;
				} else if (node instanceof EndNode) {
					x = x == 0 ? 500 : x;
					y = y == 0 ? 200 : y;
				}
				final TaskCell tc = new TaskCell((AbstractTaskNode) node, x, y);
				taskCells.put(tc.getId(), tc);
				graph.addCell(tc);
			}
		}

		for (final Node node : nodes) {
			if (node instanceof TransitionNode) {
				final TransitionNode transition = (TransitionNode) node;

				final double x = node.getDoubleProperty("x");
				final double y = node.getDoubleProperty("y");
				final double offsetX = transition.getDoubleProperty("offset-x");
				final double offsetY = transition.getDoubleProperty("offset-y");

				final List<mxPoint> points = new ArrayList<mxPoint>();
				for (final String pointStr : StringUtils.split(transition.getProperty("points"), ";")) {
					final String[] arr = StringUtils.split(pointStr, ",");
					if (arr.length == 2) {
						final mxPoint point = new mxPoint(Convert.toDouble(arr[0], 0),
								Convert.toDouble(arr[1], 0));
						points.add(point);
					}
				}

				final TransitionCell cell = new TransitionCell((TransitionNode) node, x, y, points,
						new mxPoint(offsetX, offsetY));
				graph.addEdge(cell, null, taskCells.get(transition.getFrom()),
						taskCells.get(transition.getTo()), null);
			}
		}

		return graph;
	}

	public static void writePNG(final mxGraph graph, final OutputStream oStream) throws IOException {
		final BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 1, null, true,
				null);
		ImageIO.write(image, "PNG", oStream);
	}
}
