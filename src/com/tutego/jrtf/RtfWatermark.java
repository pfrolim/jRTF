/*
 * Copyright (c) 2010-2011 Christian Ullenboom 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jRTF' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.tutego.jrtf;

/**
 * Class representing a watermark with factory methods and definitions.
 * 
 * @author pedro.costa@xseed.com.br
 */
public class RtfWatermark {
	
	/**
	 * Options for text watermarks.
	 * @author pedro.costa@xseed.com.br
	 */
	public static enum Text {
		AMOSTRA,
		CONFIDENCIAL,
		CÓPIA,
		NÃO_COPIAR {@Override public String toString(){return "NÃO COPIAR";}},
		ORIGINAL,
		PESSOAL,
		RASCUNHO,
		ULTRA_SECRETO {@Override public String toString(){return "ULTRA-SECRETO";}},
		URGENTE,
		ASAP,
		CONFIDENTIAL,
		COPY,
		DO_NOT_COPY {@Override public String toString(){return "DO NOT COPY";}},
		DRAFT,
		PERSONAL,
		SAMPLE,
		TOP_SECRET {@Override public String toString(){return "TOP SECRET";}},
		URGENT;
	}
	
	/**
	 * Options for watermark layout.
	 * @author pedro.costa@xseed.com.br
	 */
	public static enum Layout {
		Diagonal, Horizontal;
	}
	
	/**
	 * Template for creating simple text watermarks.
	 */
	private static final String TEMPLATE = "{\\\\shp{\\\\*\\\\shpinst\\\\shpleft0\\\\shptop0\\\\shpright9591\\\\shpbottom2397\\\\shpfhdr0\\\\shpbxcolumn\\\\shpbxignore\\\\shpbypara\\\\shpbyignore\\\\shpwr3\\\\shpwrk0\\\\shpfblwtxt0\\\\shpz2\\\\shplid2051"
			+ "{\\\\sp{\\\\sn shapeType}{\\\\sv 136}}"
			+ "{\\\\sp{\\\\sn fFlipH}{\\\\sv 0}}"
			+ "{\\\\sp{\\\\sn fFlipV}{\\\\sv 0}}"
			+ "{\\\\sp{\\\\sn gtextUNICODE}{\\\\sv %%TEXT%%}}"
			+ "{\\\\sp{\\\\sn gtextSize}{\\\\sv %%SIZE%%}}"
			+ "{\\\\sp{\\\\sn gtextFont}{\\\\sv %%FONT%%}}"
			+ "{\\\\sp{\\\\sn gtextFReverseRows}{\\\\sv 0}}"
			+ "{\\\\sp{\\\\sn fGtext}{\\\\sv 1}}"
			+ "{\\\\sp{\\\\sn gtextFNormalize}{\\\\sv 0}}"
			+ "{\\\\sp{\\\\sn fillColor}{\\\\sv %%COLR%%}}"
			+ "{\\\\sp{\\\\sn fFilled}{\\\\sv 1}}"
			+ "{\\\\sp{\\\\sn fLine}{\\\\sv 0}}"
			+ "{\\\\sp{\\\\sn wzName}{\\\\sv PowerPlusWaterMarkObject3}}"
			+ "{\\\\sp{\\\\sn posh}{\\\\sv 2}}"
			+ "{\\\\sp{\\\\sn posrelh}{\\\\sv 0}}"
			+ "{\\\\sp{\\\\sn posv}{\\\\sv 2}}"
			+ "{\\\\sp{\\\\sn posrelv}{\\\\sv 0}}"
			+ "{\\\\sp{\\\\sn fLayoutInCell}{\\\\sv 0}}"
			+ "{\\\\sp{\\\\sn fBehindDocument}{\\\\sv 1}}"
			+ "{\\\\sp{\\\\sn fLayoutInCell}{\\\\sv 0}}";
	
	private Text text;
	private String font;
	private int size;
	private long color;
	private boolean isSemitransparent;
	private Layout layout;
	
	/**
	 * Private constructor. Instances can be obtained with factory
	 * {@link #textWaterMark(Text)}.
	 * @param text
	 */
	private RtfWatermark(Text text) {
		this.text = text;
		// deafults
		this.font = "Times New Roman";
		this.size = 65536;
		this.color = 12632256;
		this.isSemitransparent = true;
		this.layout = Layout.Diagonal;
	}
	
	/**
	 * @return RTF for this watermark. Must be included in a header definition.
	 */
	public CharSequence rtf() {
		StringBuilder b = new StringBuilder(TEMPLATE
				.replace("%%TEXT%%", text.toString())
				.replace("%%FONT%%", font)
				.replace("%%SIZE%%", Integer.toString(size))
				.replace("%%COLR%%", Long.toString(color)));
		
		if (Layout.Diagonal.equals(layout)) {
			b.append("{\\\\sp{\\\\sn rotation}{\\\\sv 20643840}}");
		}
		
		if (isSemitransparent) {
			b.append("{\\\\sp{\\\\sn fillOpacity}{\\\\sv 32768}}");
		}
		
		return b.append("}}");
	}
	
	/**
	 * Builds a text watermark with default properties.
	 * @param text {@link Text} of this watermark.
	 * @return {@link RtfWatermark} instance.
	 */
	public static RtfWatermark textWaterMark(Text text) {
		return new RtfWatermark(text);
	}
	
	/**
	 * Change font of this watermark.
	 * @param fontName the name of the new font.
	 * @return {@code this}-object.
	 */
	public RtfWatermark font(String fontName) {
		this.font = fontName;
		return this;
	}
	
	/**
	 * Change the size of this watermark's text.
	 * @param points new size of the text.
	 * @return {@code this}-object.
	 */
	public RtfWatermark size(int points) {
		// TODO validate for maximum size
		this.size = points < 1 ? 65536 : points * 65536;
		return this;
	}
	
	/**
	 * Changes the color of this watermark's text using RGB pallete..
	 * @param r 0-255 int representing red portion of this color.
	 * @param g 0-255 int representing green portion of this color.
	 * @param b 0-255 int representing blue portion of this color.
	 * @return {@code this}-object.
	 */
	public RtfWatermark color(int r, int g, int b) {
		// TODO
		return this;
	}
	
	/**
	 * Indicates if this watermark's text is semi-transparent or not.
	 * @param isSemitransparent boolean
	 * @return {@code this}-object.
	 */
	public RtfWatermark semitransparent(boolean isSemitransparent) {
		this.isSemitransparent = isSemitransparent;
		return this;
	}
	
	/**
	 * Changes the layout of this watermark's text between diagonal or 
	 * horizontal.
	 * @param layout {@link Layout} option.
	 * @return {@code this}-object.
	 */
	public RtfWatermark layout(Layout layout) {
		this.layout = layout;
		return this;
	}
}