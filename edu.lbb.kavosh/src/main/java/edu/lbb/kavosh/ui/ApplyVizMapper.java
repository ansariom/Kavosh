/**
@project Lbb_Kavosh
@author Mitra Ansari
@date Jun 26, 2011
 **/
package edu.lbb.kavosh.ui;

import java.awt.Color;

import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.view.CyNetworkView;
import cytoscape.visual.ArrowShape;
import cytoscape.visual.CalculatorCatalog;
import cytoscape.visual.EdgeAppearanceCalculator;
import cytoscape.visual.GlobalAppearanceCalculator;
import cytoscape.visual.NodeAppearanceCalculator;
import cytoscape.visual.NodeShape;
import cytoscape.visual.VisualMappingManager;
import cytoscape.visual.VisualPropertyType;
import cytoscape.visual.VisualStyle;
import cytoscape.visual.calculators.BasicCalculator;
import cytoscape.visual.calculators.Calculator;
import cytoscape.visual.mappings.BoundaryRangeValues;
import cytoscape.visual.mappings.ContinuousMapping;
import cytoscape.visual.mappings.DiscreteMapping;
import cytoscape.visual.mappings.Interpolator;
import cytoscape.visual.mappings.LinearNumberToColorInterpolator;
import cytoscape.visual.mappings.PassThroughMapping;

public class ApplyVizMapper {
	public static final String vsName = "KavosH Visual Style";

	public ApplyVizMapper() {
	}

	public void apply(CyNetworkView networkView, CyNetwork network) {
		VisualMappingManager manager = Cytoscape.getVisualMappingManager();
		CalculatorCatalog catalog = manager.getCalculatorCatalog();

		// check to see if a visual style with this name already exists
		VisualStyle vs = catalog.getVisualStyle(vsName);
		if (vs == null) {
			// if not, create it and add it to the catalog
			vs = createVisualStyle(networkView, network);
			catalog.addVisualStyle(vs);
		}
		networkView.setVisualStyle(vs.getName());
		manager.setVisualStyle(vs);
		networkView.redrawGraph(true, true);
	}

	VisualStyle createVisualStyle(CyNetworkView networkView, CyNetwork network) {

		VisualStyle vs = networkView.getVisualStyle();
		NodeAppearanceCalculator nodeAppCalc = vs.getNodeAppearanceCalculator();
		EdgeAppearanceCalculator edgeAppCalc = vs.getEdgeAppearanceCalculator();
		GlobalAppearanceCalculator globalAppCalc = vs
				.getGlobalAppearanceCalculator();

		// Passthrough Mapping - set node label
		// PassThroughMapping pm = new PassThroughMapping(new String(),
		// "attr2");
		PassThroughMapping pm = new PassThroughMapping(String.class,
				"attr2asdf");
		Calculator nlc = new BasicCalculator("Kavosh Node Label Calculator",
				pm, VisualPropertyType.NODE_LABEL);
		nodeAppCalc.setCalculator(nlc);

		// Discrete Mapping - set node shapes
		DiscreteMapping disMapping = new DiscreteMapping(NodeShape.RECT.getClass(), "attr1");
		disMapping.putMapValue(new Integer(1), NodeShape.DIAMOND);
		disMapping.putMapValue(new Integer(2), NodeShape.ELLIPSE);
		disMapping.putMapValue(new Integer(3), NodeShape.TRIANGLE);
		
		Calculator shapeCalculator = new BasicCalculator(
				"Kavosh Node Shape Calculator", disMapping,
				VisualPropertyType.NODE_SHAPE);
		nodeAppCalc.setCalculator(shapeCalculator);

		// Continuous Mapping - set node color
		// ContinuousMapping continuousMapping = new ContinuousMapping(
		// Color.WHITE, ObjectMapping.NODE_MAPPING);
		ContinuousMapping continuousMapping = new ContinuousMapping(Color.WHITE.getClass(),
				"attr3");

		Interpolator numToColor = new LinearNumberToColorInterpolator();
		continuousMapping.setInterpolator(numToColor);

		Color underColor = Color.GRAY;
		Color minColor = Color.RED;
		Color midColor = Color.WHITE;
		Color maxColor = Color.GREEN;
		Color overColor = Color.BLUE;

		// Create boundary conditions less than, equals, greater than
		BoundaryRangeValues bv0 = new BoundaryRangeValues(underColor, minColor,
				minColor);
		BoundaryRangeValues bv1 = new BoundaryRangeValues(midColor, midColor,
				midColor);
		BoundaryRangeValues bv2 = new BoundaryRangeValues(maxColor, maxColor,
				overColor);

		// Set the attribute point values associated with the boundary values
		continuousMapping.addPoint(0.0, bv0);
		continuousMapping.addPoint(1.0, bv1);
		continuousMapping.addPoint(2.0, bv2);

		Calculator nodeColorCalculator = new BasicCalculator(
				"Kavosh Node Color Calc", continuousMapping,
				VisualPropertyType.NODE_FILL_COLOR);
		nodeAppCalc.setCalculator(nodeColorCalculator);

		// Discrete Mapping - Set edge target arrow shape
		DiscreteMapping arrowMapping = new DiscreteMapping(ArrowShape.NONE.getClass(),
				"interaction");
		arrowMapping.putMapValue("pp", ArrowShape.ARROW);
		arrowMapping.putMapValue("pd", ArrowShape.CIRCLE);

		Calculator edgeArrowCalculator = new BasicCalculator(
				"Kavosh Edge Arrow Shape Calculator", arrowMapping,
				VisualPropertyType.EDGE_TGTARROW_SHAPE);
		edgeAppCalc.setCalculator(edgeArrowCalculator);

		// Create the visual style
		VisualStyle visualStyle = new VisualStyle(vsName, nodeAppCalc,
				edgeAppCalc, globalAppCalc);

		return visualStyle;
	}
}
