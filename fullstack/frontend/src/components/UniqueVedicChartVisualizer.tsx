// src/components/UniqueVedicChartVisualizer.tsx - TYPESCRIPT ERRORS FIXED
import React, { useRef, useEffect, useState, useCallback } from 'react';
import * as d3 from 'd3';

// ✅ FIXED: Define proper types
type ZodiacSign = "Aries" | "Taurus" | "Gemini" | "Cancer" | "Leo" | "Virgo" | 
                  "Libra" | "Scorpio" | "Sagittarius" | "Capricorn" | "Aquarius" | "Pisces";

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

interface TooltipData {
  planet: string;
  degree: number;
  sign: string;
  house: number;
  isRetrograde: boolean;
}

interface HousePosition {
  house: number;
  x: number;
  y: number;
  width: number;
  height: number;
}

// ✅ FIXED: Remove 'as const' to allow string compatibility
const ZODIAC_SIGNS: string[] = [
  "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
  "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"
];

// ✅ FIXED: Add validation function for zodiac signs
const isValidZodiacSign = (sign: string): sign is ZodiacSign => {
  return ZODIAC_SIGNS.includes(sign);
};

// ✅ FIXED: Safe zodiac sign getter
const getZodiacSignIndex = (sign: string): number => {
  if (!isValidZodiacSign(sign)) {
    console.warn(`Invalid zodiac sign: ${sign}, defaulting to Aries`);
    return 0; // Default to Aries
  }
  return ZODIAC_SIGNS.indexOf(sign);
};

const CHART_CONFIG = {
  width: 600,
  height: 600,
  size: 120,
  strokeWidth: 2,
  innerStrokeWidth: 1,
  fontSize: {
    title: 18,
    houseNumber: 14,
    sign: 10,
    planet: 12,
    retrograde: 8
  },
  colors: {
    border: '#2c3e50',
    innerLines: '#34495e',
    houseNumber: '#2c3e50',
    sign: '#7f8c8d',
    planet: '#2c3e50',
    retrograde: '#e74c3c',
    aspectStrong: '#9b59b6',
    aspectWeak: '#bdc3c7'
  }
} as const;

export const UniqueVedicChartVisualizer: React.FC<Props> = ({ data }) => {
  const svgRef = useRef<SVGSVGElement>(null);
  const containerRef = useRef<HTMLDivElement>(null);
  const [tooltip, setTooltip] = useState<{
    visible: boolean;
    content: TooltipData | null;
    x: number;
    y: number;
  }>({
    visible: false,
    content: null,
    x: 0,
    y: 0
  });

  // ✅ FIXED: Enhanced validation with proper type checking
  const validateData = useCallback((data: ChartVisualizationData): boolean => {
    if (!data) return false;
    if (!data.houses || data.houses.length === 0) return false;
    if (!data.planets || data.planets.length === 0) return false;
    
    const ascendantHouse = data.houses.find(h => h.houseNumber === 1);
    if (!ascendantHouse) return false;
    
    return isValidZodiacSign(ascendantHouse.sign);
  }, []);

  // ✅ FIXED: Calculate chart layout with proper type handling
  const calculateChartLayout = useCallback((data: ChartVisualizationData) => {
    const centerX = CHART_CONFIG.width / 2;
    const centerY = CHART_CONFIG.height / 2;
    const size = CHART_CONFIG.size;

    // Find ascendant and calculate sign sequence
    const ascendantHouse = data.houses.find(h => h.houseNumber === 1)!;
    const ascendantSign = ascendantHouse.sign;
    const ascendantIndex = getZodiacSignIndex(ascendantSign);
    
    const chartSigns = Array.from({ length: 12 }, (_, i) => 
      ZODIAC_SIGNS[(ascendantIndex + i) % 12]
    );

    // Traditional North Indian Chart Layout
    const housePositions: HousePosition[] = [
      { house: 12, x: centerX - size * 1.5, y: centerY - size * 1.5, width: size * 0.8, height: size * 0.8 },
      { house: 1, x: centerX, y: centerY - size * 1.5, width: size * 0.8, height: size * 0.8 },
      { house: 2, x: centerX + size * 1.5, y: centerY - size * 1.5, width: size * 0.8, height: size * 0.8 },
      { house: 11, x: centerX - size * 1.5, y: centerY, width: size * 0.8, height: size * 0.8 },
      { house: 3, x: centerX + size * 1.5, y: centerY, width: size * 0.8, height: size * 0.8 },
      { house: 10, x: centerX - size * 1.5, y: centerY + size * 1.5, width: size * 0.8, height: size * 0.8 },
      { house: 9, x: centerX, y: centerY + size * 1.5, width: size * 0.8, height: size * 0.8 },
      { house: 8, x: centerX + size * 1.5, y: centerY + size * 1.5, width: size * 0.8, height: size * 0.8 },
      { house: 4, x: centerX - size * 0.5, y: centerY - size * 0.5, width: size * 0.6, height: size * 0.6 },
      { house: 5, x: centerX + size * 0.5, y: centerY - size * 0.5, width: size * 0.6, height: size * 0.6 },
      { house: 6, x: centerX + size * 0.5, y: centerY + size * 0.5, width: size * 0.6, height: size * 0.6 },
      { house: 7, x: centerX - size * 0.5, y: centerY + size * 0.5, width: size * 0.6, height: size * 0.6 }
    ];

    return { chartSigns, housePositions, centerX, centerY, size };
  }, []);

  // ✅ FIXED: Group planets with proper string handling
  const groupPlanetsBySign = useCallback((planets: ChartVisualizationData['planets'], chartSigns: string[]) => {
    const planetsBySign = new Map<string, typeof planets>();
    
    planets.forEach(planet => {
      // ✅ FIXED: Validate planet sign before using
      const planetSign = isValidZodiacSign(planet.sign) ? planet.sign : 'Aries';
      
      if (!planetsBySign.has(planetSign)) {
        planetsBySign.set(planetSign, []);
      }
      planetsBySign.get(planetSign)!.push(planet);
    });

    return planetsBySign;
  }, []);

  const showTooltip = useCallback((event: MouseEvent, planet: TooltipData) => {
    const rect = containerRef.current?.getBoundingClientRect();
    if (!rect) return;

    setTooltip({
      visible: true,
      content: planet,
      x: event.clientX - rect.left + 10,
      y: event.clientY - rect.top - 10
    });
  }, []);

  const hideTooltip = useCallback(() => {
    setTooltip(prev => ({ ...prev, visible: false }));
  }, []);

  useEffect(() => {
    if (!data || !svgRef.current || !validateData(data)) {
      console.warn('UniqueVedicChartVisualizer: Invalid or missing data');
      return;
    }

    const svg = d3.select(svgRef.current);
    svg.selectAll("*").remove();

    const { chartSigns, housePositions, centerX, centerY, size } = calculateChartLayout(data);
    
    svg
      .attr("width", CHART_CONFIG.width)
      .attr("height", CHART_CONFIG.height)
      .attr("viewBox", `0 0 ${CHART_CONFIG.width} ${CHART_CONFIG.height}`)
      .style("background", "#ffffff");

    // Draw main diamond outline
    const diamondPoints = [
      [centerX, centerY - size * 2],
      [centerX + size * 2, centerY],
      [centerX, centerY + size * 2],
      [centerX - size * 2, centerY]
    ];

    svg.append("polygon")
      .attr("points", diamondPoints.map(p => p.join(",")).join(" "))
      .attr("fill", "none")
      .attr("stroke", CHART_CONFIG.colors.border)
      .attr("stroke-width", CHART_CONFIG.strokeWidth);

    // Draw internal house division lines
    const internalLines = [
      [[centerX - size * 2, centerY - size], [centerX + size * 2, centerY - size]],
      [[centerX - size * 2, centerY + size], [centerX + size * 2, centerY + size]],
      [[centerX - size, centerY - size * 2], [centerX - size, centerY + size * 2]],
      [[centerX + size, centerY - size * 2], [centerX + size, centerY + size * 2]],
      [[centerX - size, centerY], [centerX + size, centerY]],
      [[centerX, centerY - size], [centerX, centerY + size]]
    ];

    internalLines.forEach(([[x1, y1], [x2, y2]]) => {
      svg.append("line")
        .attr("x1", x1)
        .attr("y1", y1)
        .attr("x2", x2)
        .attr("y2", y2)
        .attr("stroke", CHART_CONFIG.colors.innerLines)
        .attr("stroke-width", CHART_CONFIG.innerStrokeWidth);
    });

    // Draw chart title
    svg.append("text")
      .attr("x", centerX)
      .attr("y", 25)
      .attr("text-anchor", "middle")
      .attr("font-family", "Arial, sans-serif")
      .attr("font-size", CHART_CONFIG.fontSize.title)
      .attr("font-weight", "bold")
      .attr("fill", CHART_CONFIG.colors.border)
      .text("Vedic Birth Chart (D1)");

    // Draw house numbers and zodiac signs
    housePositions.forEach(({ house, x, y }) => {
      const signIndex = house - 1;
      const zodiacSign = chartSigns[signIndex];

      // House number
      svg.append("text")
        .attr("x", x)
        .attr("y", y - 20)
        .attr("text-anchor", "middle")
        .attr("font-family", "Arial, sans-serif")
        .attr("font-size", CHART_CONFIG.fontSize.houseNumber)
        .attr("font-weight", "bold")
        .attr("fill", CHART_CONFIG.colors.houseNumber)
        .text(house.toString());

      // Zodiac sign abbreviation
      svg.append("text")
        .attr("x", x)
        .attr("y", y - 5)
        .attr("text-anchor", "middle")
        .attr("font-family", "Arial, sans-serif")
        .attr("font-size", CHART_CONFIG.fontSize.sign)
        .attr("fill", CHART_CONFIG.colors.sign)
        .text(zodiacSign.substring(0, 2).toUpperCase());
    });

    // ✅ FIXED: Place planets with safe sign handling
    const planetsBySign = groupPlanetsBySign(data.planets, chartSigns);
    
    planetsBySign.forEach((planets, sign) => {
      // ✅ FIXED: Safe indexOf with validation
      const houseIndex = chartSigns.findIndex(s => s === sign);
      if (houseIndex === -1) return;
      
      const housePosition = housePositions[houseIndex];
      if (!housePosition) return;

      planets.forEach((planet, planetIndex) => {
        const planetY = housePosition.y + 15 + (planetIndex * 15);
        
        const planetText = svg.append("text")
          .attr("x", housePosition.x)
          .attr("y", planetY)
          .attr("text-anchor", "middle")
          .attr("font-family", "Arial, sans-serif")
          .attr("font-size", CHART_CONFIG.fontSize.planet)
          .attr("fill", planet.color || CHART_CONFIG.colors.planet)
          .attr("cursor", "pointer")
          .text(planet.planet.substring(0, 2).toUpperCase())
          .on("mouseover", function(event: MouseEvent) {
            d3.select(this).attr("font-weight", "bold");
            showTooltip(event, {
              planet: planet.planet,
              degree: planet.degree,
              sign: planet.sign,
              house: planet.house,
              isRetrograde: planet.isRetrograde
            });
          })
          .on("mouseout", function() {
            d3.select(this).attr("font-weight", "normal");
            hideTooltip();
          });

        if (planet.isRetrograde) {
          svg.append("text")
            .attr("x", housePosition.x + 20)
            .attr("y", planetY - 2)
            .attr("font-family", "Arial, sans-serif")
            .attr("font-size", CHART_CONFIG.fontSize.retrograde)
            .attr("fill", CHART_CONFIG.colors.retrograde)
            .attr("font-weight", "bold")
            .text("R");
        }
      });
    });

    // ✅ FIXED: Draw aspects with safe sign handling
    if (data.aspects && data.aspects.length > 0) {
      data.aspects.forEach(aspect => {
        const fromPlanet = data.planets.find(p => p.planet === aspect.fromPlanet);
        const toPlanet = data.planets.find(p => p.planet === aspect.toPlanet);
        
        if (!fromPlanet || !toPlanet) return;

        // ✅ FIXED: Safe indexOf with validation
        const fromSignIndex = chartSigns.findIndex(s => s === fromPlanet.sign);
        const toSignIndex = chartSigns.findIndex(s => s === toPlanet.sign);
        
        if (fromSignIndex === -1 || toSignIndex === -1) return;

        const fromPos = housePositions[fromSignIndex];
        const toPos = housePositions[toSignIndex];
        
        if (!fromPos || !toPos) return;

        svg.append("line")
          .attr("x1", fromPos.x)
          .attr("y1", fromPos.y)
          .attr("x2", toPos.x)
          .attr("y2", toPos.y)
          .attr("stroke", aspect.strength === "strong" ? CHART_CONFIG.colors.aspectStrong : CHART_CONFIG.colors.aspectWeak)
          .attr("stroke-width", aspect.strength === "strong" ? 2 : 1)
          .attr("opacity", 0.6)
          .attr("stroke-dasharray", aspect.aspectType === "conjunction" ? "" : "5,5");
      });
    }

    // Add Swiss Ephemeris precision indicator
    svg.append("text")
      .attr("x", centerX)
      .attr("y", CHART_CONFIG.height - 10)
      .attr("text-anchor", "middle")
      .attr("font-family", "Arial, sans-serif")
      .attr("font-size", 10)
      .attr("fill", CHART_CONFIG.colors.sign)
      .text("Swiss Ephemeris Precision • NASA-JPL Accuracy");

  }, [data, validateData, calculateChartLayout, groupPlanetsBySign, showTooltip, hideTooltip]);

  useEffect(() => {
    return () => {
      setTooltip(prev => ({ ...prev, visible: false }));
    };
  }, []);

  if (!data || !validateData(data)) {
    return (
      <div className="w-full max-w-2xl mx-auto bg-gradient-to-br from-slate-100 to-slate-200 rounded-lg shadow-lg p-8">
        <div className="text-center">
          <div className="w-16 h-16 mx-auto mb-4 rounded-full bg-gradient-to-br from-orange-400 to-red-500 flex items-center justify-center">
            <span className="text-2xl">⚠️</span>
          </div>
          <h3 className="text-lg font-semibold text-slate-700 mb-2">Chart Data Not Available</h3>
          <p className="text-slate-600">
            Please ensure your birth details are complete and accurate for chart generation.
          </p>
        </div>
      </div>
    );
  }

  return (
    <div 
      ref={containerRef}
      className="relative w-full max-w-2xl mx-auto bg-gradient-to-br from-white to-slate-50 rounded-lg shadow-2xl p-6 border border-slate-200"
    >
      <svg 
        ref={svgRef} 
        className="w-full h-auto"
        style={{
          maxWidth: '600px',
          height: '600px',
          filter: 'drop-shadow(0 4px 6px rgba(0, 0, 0, 0.1))'
        }}
      />
      
      {tooltip.visible && tooltip.content && (
        <div
          className="absolute z-10 p-3 bg-slate-900 text-white text-sm rounded-lg shadow-xl border border-slate-700 pointer-events-none"
          style={{
            left: `${tooltip.x}px`,
            top: `${tooltip.y}px`,
            maxWidth: '200px'
          }}
        >
          <div className="font-semibold text-blue-200">{tooltip.content.planet}</div>
          <div className="text-xs space-y-1 mt-1">
            <div>📍 {tooltip.content.degree.toFixed(2)}° in {tooltip.content.sign}</div>
            <div>🏠 House {tooltip.content.house}</div>
            {tooltip.content.isRetrograde && (
              <div className="text-red-300 font-medium">⟲ Retrograde Motion</div>
            )}
          </div>
          <div className="text-xs text-slate-400 mt-2">Swiss Ephemeris Precision</div>
        </div>
      )}
    </div>
  );
};

export default UniqueVedicChartVisualizer;
