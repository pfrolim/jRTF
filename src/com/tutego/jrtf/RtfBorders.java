package com.tutego.jrtf;

/**
 * Paragraph and cell border styles.
 * 
 * @author pedro.costa@xseed.com.br
 *
 */
public enum RtfBorders {
	
	/** Single-thickness border. */
	SINGLE_THICKNESS {@Override public String toRtf() { return "\\brdrs"; }}
	/** Double-thickness border. */
	,DOUBLE_THICKNESS {@Override public String toRtf() { return "\\brdrth"; }}
	/** Shadowed border. */
	,SHADOWED {@Override public String toRtf() { return "\\brdrsh"; }}
	/** Double border. */
	,DOUBLE {@Override public String toRtf() { return "\\brdrdb"; }}
	/** Dotted border. */
	,DOTTED {@Override public String toRtf() { return "\\brdrdot"; }}
	/** Dashed border. */
	,DASHED {@Override public String toRtf() { return "\\brdrdash"; }}
	/** Hairline border. */
	,HAIRLINE {@Override public String toRtf() { return "\\brdrhair"; }}
	/** Inset border.*/
	,INSET {@Override public String toRtf() { return "\\brdrinset"; }}
	/** Dash border (small). */
	,SMALL_DASHED {@Override public String toRtf() { return "\\brdrdashsm"; }}
	/** Dot dash border. */
	,DOT_DASHED {@Override public String toRtf() { return "\\brdrdashd"; }}
	/** Dot dot dash border. */
	,DOT_DOT_DASHED {@Override public String toRtf() { return "\\brdrdashdd"; }}
	/** Outset border. */
	,OUTSET {@Override public String toRtf() { return "\\brdroutset"; }}
	/** Triple border. */
	,TRIPLE {@Override public String toRtf() { return "\\brdrtriple"; }}
	/** Thick thin border (small). */
	,SMALL_THICK_THIN {@Override public String toRtf() { return "\\brdrtnthsg"; }}
	/** Thin thick border (small). */
	,SMALL_THIN_THICK {@Override public String toRtf() { return "\\brdrthtnsg"; }}
	/** Thin thick thin border (small).*/
	,SMALL_THIN_THICK_THIN {@Override public String toRtf() { return "\\brdrtnthtnsg"; }}
	/** Thick thin border (medium).*/
	,MEDIUM_THICK_THIN {@Override public String toRtf() { return "\\brdrtnthmg"; }}
	/** Thin thick border (medium).*/
	,MEDIUM_THIN_THICK {@Override public String toRtf() { return "\\brdrthtnmg"; }}
	/** Thin thick thin border (medium). */
	,MEDIUM_THIN_THICK_THIN {@Override public String toRtf() { return "\\brdrtnthtnmg"; }}
	/** Thick thin border (large). */
	,LARGE_THICK_THIN {@Override public String toRtf() { return "\\brdrtnthlg"; }}
	/** Thin thick border (large). */
	,LARGE_THIN_THICK {@Override public String toRtf() { return "\\brdrthtnlg"; }}
	/** Thin thick thin border (large).*/
	,LARGE_THIN_THICK_THIN {@Override public String toRtf() { return "\\brdrtnthtnlg"; }}
	/** Wavy border. */
	,WAVY {@Override public String toRtf() { return "\\brdrwavy"; }}
	/** Double wavy border.*/
	,DOUBLE_WAVY {@Override public String toRtf() { return "\\brdrwavydb"; }}
	/** Striped border.*/
	,STRIPED {@Override public String toRtf() { return "\\brdrdashdotstr"; }}
	/** Emboss border. */
	,EMBOSS {@Override public String toRtf() { return "\\brdremboss"; }}
	/** Engrave border.*/
	,ENGRAVE {@Override public String toRtf() { return "\\brdrengrave"; }}
	/** Border resembles a "Frame."*/
	,FRAME {@Override public String toRtf() { return "\\brdrframe"; }}
	;
	
	/**
	 * @return RTF control word of this border style.
	 */
	public abstract String toRtf();

}
