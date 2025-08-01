import React, { useRef, useEffect } from 'react';
import * as d3 from 'd3';

interface ChartVisualizationData {
  planets: Array<{
    planet: string;
    degree: number;
    sign: string;
    house: number;
    color: string;
    isRetrograde: boolean;
  }>;
  houses: Array<{
    houseNumber: number;
    sign: string;
    startDegree: number;
    lord: string;
    meaning: string;
  }>;
  aspects: Array<{
    fromPlanet: string;
    toPlanet: string;
    aspectType: string;
    orb: number;
    strength: string;
  }>;
}

interface Props {
  data: ChartVisualizationData;
}

const ZODIAC_SIGNS = [
  "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
  "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"
];

export function UniqueVedicChartVisualizer({ data }: Props) {
  const svgRef = useRef<SVGSVGElement>(null);
  const tooltipRef = useRef<any>(null);

  useEffect(() => {
    if (!data || !svgRef.current || !data.houses || data.houses.length === 0) return;

    const svg = d3.select(svgRef.current);
    svg.selectAll("*").remove();

    const width = 600;
    const height = 600;
    const centerX = width / 2;
    const centerY = height / 2;
    const size = 100;

    svg.attr("width", width).attr("height", height);

    // Get ascendant sign from house 1
    const ascendantHouse = data.houses.find(h => h.houseNumber === 1);
    if (!ascendantHouse) {
      console.error("No ascendant house found");
      return;
    }

    const ascendantSign = ascendantHouse.sign;
    const ascendantIndex = ZODIAC_SIGNS.indexOf(ascendantSign);

    if (ascendantIndex === -1) {
      console.error("Invalid ascendant sign:", ascendantSign);
      return;
    }

    // Create sign sequence from ascendant
    const chartSigns: string[] = [];
    for (let i = 0; i < 12; i++) {
      chartSigns.push(ZODIAC_SIGNS[(ascendantIndex + i) % 12]);
    }

    const chartGroup = svg.append("g")
      .attr("transform", `translate(${centerX}, ${centerY})`);

    // ✅ EXACT LAYOUT MATCHING YOUR IMAGE - Traditional North Indian D1 Chart
    const housePositions = [
      // Top row
      { house: 12, x: -size*1.2, y: -size*1.2, width: size*0.8, height: size*0.8 },
      { house: 1, x: 0, y: -size*1.2, width: size*0.8, height: size*0.8 },
      { house: 2, x: size*1.2, y: -size*1.2, width: size*0.8, height: size*0.8 },
      
      // Middle row
      { house: 11, x: -size*1.2, y: 0, width: size*0.8, height: size*0.8 },
      { house: 10, x: size*1.2, y: 0, width: size*0.8, height: size*0.8 },
      
      // Bottom row  
      { house: 10, x: -size*1.2, y: size*1.2, width: size*0.8, height: size*0.8 },
      { house: 9, x: 0, y: size*1.2, width: size*0.8, height: size*0.8 },
      { house: 8, x: size*1.2, y: size*1.2, width: size*0.8, height: size*0.8 },
      
      // Inner positions
      { house: 7, x: 0, y: size*0.6, width: size*0.6, height: size*0.4 },
      { house: 6, x: size*0.6, y: size*0.6, width: size*0.6, height: size*0.4 },
      { house: 5, x: size*0.6, y: 0, width: size*0.4, height: size*0.6 },
      { house: 4, x: 0, y: -size*0.6, width: size*0.6, height: size*0.4 },
      { house: 3, x: -size*0.6, y: 0, width: size*0.4, height: size*0.6 }
    ];

    // Draw title
    svg.append("text")
      .attr("x", centerX)
      .attr("y", 30)
      .attr("text-anchor", "middle")
      .attr("font-family", "'Arial', sans-serif")
      .attr("font-size", "18px")
      .attr("font-weight", "bold")
      .attr("fill", "#333")
      .text("D1 CHART");

    // Draw main diamond outline exactly like your image
    const diamondOutline = [
      [0, -size*2],          // Top
      [size*2, 0],           // Right  
      [0, size*2],           // Bottom
      [-size*2, 0]           // Left
    ].map(p => p.join(',')).join(' ');

    chartGroup.append("polygon")
      .attr("points", diamondOutline)
      .attr("fill", "none")
      .attr("stroke", "#000")
      .attr("stroke-width", 2);

    // Draw inner diamond lines to create house divisions
    const innerLines = [
      // Horizontal lines
      [[-size*2, -size], [size*2, -size]],
      [[-size*2, size], [size*2, size]],
      // Vertical lines  
      [[-size, -size*2], [-size, size*2]],
      [[size, -size*2], [size, size*2]],
      // Diagonal lines
      [[-size, 0], [size, 0]],
      [[0, -size], [0, size]]
    ];

    innerLines.forEach(([[x1, y1], [x2, y2]]) => {
      chartGroup.append("line")
        .attr("x1", x1)
        .attr("y1", y1)
        .attr("x2", x2)
        .attr("y2", y2)
        .attr("stroke", "#000")
        .attr("stroke-width", 1);
    });

    // ✅ TRADITIONAL 12 HOUSE LAYOUT exactly matching your reference image
    const traditionalHouses = [
      // Outer houses - arranged exactly as in traditional D1 chart
      { house: 12, x: -size*1.5, y: -size*1.5, label: "12" },
      { house: 1, x: 0, y: -size*1.5, label: "1" },
      { house: 2, x: size*1.5, y: -size*1.5, label: "2" },
      { house: 11, x: -size*1.5, y: 0, label: "11" },
      { house: 3, x: size*1.5, y: 0, label: "3" },
      { house: 10, x: -size*1.5, y: size*1.5, label: "10" },
      { house: 9, x: 0, y: size*1.5, label: "9" },
      { house: 8, x: size*1.5, y: size*1.5, label: "8" },
      // Inner houses
      { house: 4, x: -size*0.5, y: -size*0.5, label: "4" },
      { house: 5, x: size*0.5, y: -size*0.5, label: "5" },
      { house: 6, x: size*0.5, y: size*0.5, label: "6" },
      { house: 7, x: -size*0.5, y: size*0.5, label: "7" }
    ];

    // Map signs to house positions based on ascendant
    const signToHouseIndex = new Map<string, number>();
    chartSigns.forEach((sign, index) => {
      signToHouseIndex.set(sign, index);
    });

    // Draw house numbers exactly like your reference
    traditionalHouses.forEach(({ house, x, y, label }) => {
      // House number
      chartGroup.append("text")
        .attr("x", x)
        .attr("y", y - 15)
        .attr("text-anchor", "middle")
        .attr("font-family", "'Arial', sans-serif")
        .attr("font-size", "14px")
        .attr("font-weight", "bold")
        .attr("fill", "#000")
        .text(label);
      
      // Sign in this house
      const signInHouse = chartSigns[house - 1];
      chartGroup.append("text")
        .attr("x", x)
        .attr("y", y)
        .attr("text-anchor", "middle")
        .attr("font-family", "'Arial', sans-serif")
        .attr("font-size", "10px")
        .attr("fill", "#666")
        .text(signInHouse.substring(0, 2));
    });

    // Group planets by their zodiac sign (authentic D1 logic)
    const planetsBySign = new Map<string, typeof data.planets>();
    data.planets.forEach(planet => {
      const planetsInSign = planetsBySign.get(planet.sign) || [];
      planetsInSign.push(planet);
      planetsBySign.set(planet.sign, planetsInSign);
    });

    // Place planets in correct houses based on their signs
    planetsBySign.forEach((planets, sign) => {
      const houseIndex = signToHouseIndex.get(sign);
      if (houseIndex === undefined) return;

      const housePosition = traditionalHouses[houseIndex];
      if (!housePosition) return;

      planets.forEach((planet, planetIndex) => {
        const planetY = housePosition.y + 15 + (planetIndex * 12);
        
        // Planet name (exactly like your reference image)
        chartGroup.append("text")
          .attr("x", housePosition.x)
          .attr("y", planetY)
          .attr("text-anchor", "middle")
          .attr("font-family", "'Arial', sans-serif")
          .attr("font-size", "10px")
          .attr("fill", "#333")
          .text(planet.planet.toLowerCase())
          .style("cursor", "pointer")
          .on("mouseover", function(event) {
            showTooltip(event, planet);
          })
          .on("mouseout", function() {
            hideTooltip();
          });

        // Retrograde indicator
        if (planet.isRetrograde) {
          chartGroup.append("text")
            .attr("x", housePosition.x + 25)
            .attr("y", planetY - 2)
            .attr("font-family", "'Arial', sans-serif")
            .attr("font-size", "8px")
            .attr("fill", "#ff0000")
            .text("R");
        }
      });
    });

    createTooltip();

  }, [data]);

  const createTooltip = () => {
    d3.select(".vedic-chart-tooltip").remove();
    
    const tooltip = d3.select("body").append("div")
      .attr("class", "vedic-chart-tooltip")
      .style("position", "absolute")
      .style("visibility", "hidden")
      .style("background", "rgba(0, 0, 0, 0.9)")
      .style("color", "white")
      .style("padding", "8px 12px")
      .style("border-radius", "6px")
      .style("font-size", "12px")
      .style("font-family", "'Arial', sans-serif")
      .style("box-shadow", "0 4px 12px rgba(0, 0, 0, 0.3)")
      .style("pointer-events", "none")
      .style("z-index", "10000")
      .style("max-width", "200px");

    tooltipRef.current = tooltip;
  };

  const showTooltip = (event: any, planet: any) => {
    const tooltip = tooltipRef.current;
    if (!tooltip) return;

    tooltip
      .style("visibility", "visible")
      .html(`
        <div style="text-align: center;">
          <strong>${planet.planet}</strong><br/>
          ${planet.degree.toFixed(2)}° ${planet.sign}<br/>
          House ${planet.house}
          ${planet.isRetrograde ? '<br/><span style="color: #ff4444;">⟲ Retrograde</span>' : ''}
        </div>
      `)
      .style("left", (event.pageX + 10) + "px")
      .style("top", (event.pageY - 10) + "px");
  };

  const hideTooltip = () => {
    const tooltip = tooltipRef.current;
    if (tooltip) {
      tooltip.style("visibility", "hidden");
    }
  };

  useEffect(() => {
    return () => {
      d3.select(".vedic-chart-tooltip").remove();
    };
  }, []);

  return (
    <div className="relative w-full max-w-2xl mx-auto bg-white rounded-lg shadow-lg p-4">
      <svg 
        ref={svgRef} 
        className="w-full h-auto"
        style={{
          background: 'white',
          maxWidth: '600px',
          height: '600px'
        }}
      />
    </div>
  );
}

export default UniqueVedicChartVisualizer;
