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

import java.io.IOException;

/**
 * 
 * @author pedro.costa@xseed.com.br
 */
public abstract class RtfCell extends RtfPara {
	
	StringBuilder celldef = new StringBuilder();
	
	String alignment = "\\ql"; // default: left
	
	/**
	 * @return {@code this}-object.
	 */
	public RtfCell alignLeft() {
		alignment = "\\ql";
		return this;
	}

	/**
	 * @return {@code this}-object.
	 */
	public RtfCell alignRight() {
		alignment = "\\qr";
		return this;
	}

	/**
	 * @return {@code this}-object.
	 */
	public RtfCell alignJustified() {
		alignment = "\\qj";
		return this;
	}

	/**
	 * @return {@code this}-object.
	 */
	public RtfCell alignCentered() {
		alignment = "\\qc";
		return this;
	}
	
	/**
	 * Preferred cell width. Overrides AutoFit.
	 * 
	 * @param width
	 *            Width of the row.
	 * @param unit
	 *            Measurement.
	 * @return {@code this}-object.
	 */
	public RtfCell width(double width, RtfUnit unit) {
		if (RtfUnit.PERCENTAGE.equals(unit)) {
			celldef.append("\\clftsWidth2\\clwWidth");
			celldef.append(new Double(width * 50).intValue());
		} else {
			celldef.append("\\clftsWidth3\\clwWidth");
			celldef.append(unit.toTwips(width));
		}

		return this;
	}

	/**
	 * The first cell in a range of table cells to be merged.
	 * @return {@code this}-object.
	 */
	public RtfCell firstMerged() {
		celldef.append("\\clmgf");
		return this;
	}

	/**
	 * Contents of the table cell are merged with those of the preceding cell.
	 * @return {@code this}-object.
	 */
	public RtfCell mergePrevious() {
		celldef.append("\\clmrg");
		return this;
	}
	
	/**
	 * Top table cell border.
	 * @param borderStyle {@link RtfBorders}
	 * @return {@code this}-object.
	 */
	public RtfCell topBorder(RtfBorders borderStyle) {
		celldef.append("\\clbrdrb");
		celldef.append(borderStyle.toRtf());
		return this;
	}
	
	/**
	 * Top table cell single thickness border (default style). 
	 * @return {@code this}-object.
	 */
	public RtfCell topBorder() {
		celldef.append("\\clbrdrb");
		celldef.append(RtfBorders.SINGLE_THICKNESS);
		return this;
	}
	
	/**
	 * Left table cell border.
	 * @param borderStyle
	 * @return
	 */
	public RtfCell leftBorder(RtfBorders borderStyle) {
		celldef.append("\\clbrdrl");
		celldef.append(borderStyle.toRtf());
		return this;
	}
	
	/**
	 * Left table cell single thickness border (default style). 
	 * @return
	 */
	public RtfCell leftBorder() {
		celldef.append("\\clbrdrl");
		celldef.append(RtfBorders.SINGLE_THICKNESS);
		return this;
	}
	
	/**
	 * Right table cell border.
	 * @param borderStyle {@link RtfBorders}
	 * @return {@code this}-object.
	 */
	public RtfCell rightBorder(RtfBorders borderStyle) {
		celldef.append("\\clbrdrr");
		celldef.append(borderStyle.toRtf());
		return this;
	}
	
	/**
	 * Right table cell single thickness border (default style).
	 * @return {@code this}-object.
	 */
	public RtfCell rightBorder() {
		celldef.append("\\clbrdrr");
		celldef.append(RtfBorders.SINGLE_THICKNESS);
		return this;
	}
	
	/**
	 * Bottom table cell border.
	 * @param borderStyle {@link RtfBorders}
	 * @return {@code this}-object.
	 */
	public RtfCell bottonBorder(RtfBorders borderStyle) {
		celldef.append("\\clbrdrb");
		celldef.append(borderStyle.toRtf());
		return this;
	}
	
	/**
	 * Bottom table cell single thickness border (default style).
	 * @return {@code this}-object.
	 */
	public RtfCell bottonBorder() {
		celldef.append("\\clbrdrb");
		celldef.append(RtfBorders.SINGLE_THICKNESS);
		return this;
	}
	
	public static RtfCell cell() {
		return cell((String) null);
	}
	
	public static RtfCell cell(final RtfText text) {
		return new RtfCell() {
			@Override
			void rtf(Appendable out, boolean withEndingPar) throws IOException {
				out.append(celldef);
				out.append("\\cellx0");
				text.rtf(out);
				out.append(alignment);
				out.append("\\intbl\\cell\n");
			}
		};
	}
	
	public static RtfCell cell(final String text) {
		return new RtfCell() {
			@Override
			void rtf(Appendable out, boolean withEndingPar) throws IOException {
				out.append(celldef);
				out.append("\\cellx0{");
				out.append(text == null ? "" : text.replaceAll("\n", "\\\\line\n")); // FIXME include other tags
				out.append("}");
				out.append(alignment);
				out.append("\\intbl\\cell\n");
			}
		};
	}
}
