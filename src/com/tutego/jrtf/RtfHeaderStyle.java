///*
// * Copyright (c) 2010-2011 Christian Ullenboom 
// * All rights reserved.
// *
// * Redistribution and use in source and binary forms, with or without
// * modification, are permitted provided that the following conditions are
// * met:
// *
// * * Redistributions of source code must retain the above copyright
// *   notice, this list of conditions and the following disclaimer.
// *
// * * Redistributions in binary form must reproduce the above copyright
// *   notice, this list of conditions and the following disclaimer in the
// *   documentation and/or other materials provided with the distribution.
// *
// * * Neither the name of 'jRTF' nor the names of its contributors
// *   may be used to endorse or promote products derived from this software
// *   without specific prior written permission.
// *
// * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
// * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
// * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
// * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
// * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
// * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
// * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
// * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
// * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
// */
//package com.tutego.jrtf;
//
//import java.io.IOException;
//
//
////       .header( 
////               parStyle( "Heading 2").fontSize( 40 ).bold().at( 2 ),
////               parStyle( "Heading 3").fontSize( 13 ).bold().at( 3 )
////               )
////           .lastSection( p("sdfgsfg").style( 1 ) );
//
//public class RtfHeaderStyle extends RtfHeader
//{
//  /*
//   * <style>  := '{'
//   *             <styledef>?
//   *             <keycode>?
//   *             <formatting>
//   *             <additive>?
//   *             <based>?
//   *             <next>?
//   *             <autoupd>?
//   *             <hidden>?
//   *             <personal>?
//   *             <compose>?
//   *             <reply>?
//   *             <styleid>?
//   *             <semihidden>?
//   *             <stylename>?
//   *             ';' '}'
//   */
//
//  private final String styledef;
//  private final StringBuilder brdrdef = new StringBuilder( 512 );
//  private final StringBuilder parfmt = new StringBuilder( 512 );
//  private final StringBuilder chrfmt = new StringBuilder( 512 );
//  private boolean additive;
//  private String basedOn;
//  private String next;
//  private final String stylename;
//  private int stylenum;
//  
//  RtfHeaderStyle( String styledef, String stylename )
//  {
//    this.styledef  = styledef;
//    this.stylename = stylename;
//  }
//
//  public RtfHeaderStyle additive()
//  {
//    additive = true;
//    return this;
//  }
//
//  public RtfHeaderStyle basedOn( int stylenumber )
//  {
//    if ( stylenumber < 0 )
//      throw new IllegalArgumentException( "Style number is not allowed to be negative" );
//
//    basedOn = "\\sbasedon" + stylenumber;
//    return this;
//  }
//
//  public RtfHeaderStyle next( int stylenumber )
//  {
//    if ( stylenumber < 0 )
//      throw new IllegalArgumentException( "Style number is not allowed to be negative" );
//
//    next = "\\snext" + stylenumber;
//    return this;
//  }
//
////  public int stylenum()
////  {
////    return stylenum;
////  }
//
//  // Formattings
//  /*
//   *  <formatting> := (<brdrdef> |
//   *                  <parfmt> |
//   *                  <apoctl> |
//   *                  <tabdef> |
//   *                  <shading> |
//   *                  <chrfmt>)+
//   */
//
//  // <parfmt>
//  
//  /**
//   * Resets to default paragraph properties.
//   * 
//   * @return
//   */
//  public RtfHeaderStyle reset()
//  {
//    parfmt.append( "\\pard" );
//    return this;
//  }
//  
//  /**
//   * Hyphenation for the paragraph on.
//   * @return
//   */
//  public RtfHeaderStyle hyphenationOn()
//  {
//    parfmt.append( "\\hyphpar1" );
//    return this;
//  }
//
//  /**
//   * Hyphenation for the paragraph off.
//   * @return
//   */
//  public RtfHeaderStyle hyphenationOff()
//  {
//    parfmt.append( "\\hyphpar0" );
//    return this;
//  }
//  
//  /**
//   * Paragraph is part of a table.
//   * @return
//   */
//  public RtfHeaderStyle partOfTable()
//  {
//    parfmt.append( "\\intbl" );
//    return this;
//  }
//
//  /**
//   * Keep paragraph intact.
//   * @return
//   */
//  public RtfHeaderStyle keep()
//  {
//    parfmt.append( "\\keep" );
//    return this;
//  }
//
//  /**
//   * No widow/orphan control.
//   * @return
//   */
//  public RtfHeaderStyle noWidowOrOrphanControl()
//  {
//    parfmt.append( "\\nowidctlpar" );
//    return this;
//  }
//
//  /**
//   * Keep paragraph with the next paragraph.
//   * @return
//   */
//  public RtfHeaderStyle keepWithNextParagraph()
//  {
//    parfmt.append( "\\keepn" );
//    return this;
//  }
//
//  /**
//   * Outline level of the paragraph.
//   * @return
//   */
//  public RtfHeaderStyle level( int level )
//  {
//    if ( level < 0 )
//      throw new IllegalArgumentException( "Level is not allowed to be negative but is " + level );
//
//    parfmt.append( "\\level" ).append( level );
//    return this;
//  }
//
//  /**
//   * No line numbering.
//   * @return
//   */
//  public RtfHeaderStyle noLineNumbering()
//  {
//    parfmt.append( "\\noline" );
//    return this;
//  }
//
//  /**
//   * Break page before the paragraph.
//   * @return
//   */
//  public RtfHeaderStyle breakPageBeforeParagraph()
//  {
//    parfmt.append( "\\pagebb" );
//    return this;
//  }
//
//  public RtfHeaderStyle alignLeft()
//  {
//    parfmt.append( "\\ql" );
//    return this;
//  }
//
//  public RtfHeaderStyle alignRight()
//  {
//    parfmt.append( "\\qr" );
//    return this;
//  }
//
//  public RtfHeaderStyle alignJustified()
//  {
//    parfmt.append( "\\qj" );
//    return this;
//  }
//
//  public RtfHeaderStyle alignCentered()
//  {
//    parfmt.append( "\\qc" );
//    return this;
//  }
//  
//  /**
//   * First-line indent by given amount.
//   * @return
//   */
//  public RtfHeaderStyle indentFirstLine( double indentation, RtfUnit unit )
//  {
//    parfmt.append( "\\fi" ).append( unit.toTwips( indentation ) );
//    return this;
//  }
//
//  /**
//   * Left indent by given amount.
//   * @return
//   */
//  public RtfHeaderStyle indentLeft( double indentation, RtfUnit unit )
//  {
//    parfmt.append( "\\li" ).append( unit.toTwips( indentation ) );
//    return this;
//  }
//
//  /**
//   * Right indent by given amount.
//   * @return
//   */
//  public RtfHeaderStyle indentRight( double indentation, RtfUnit unit )
//  {
//    parfmt.append( "\\ri" ).append( unit.toTwips( indentation ) );
//    return this;
//  }
//  
//  //
//  // Spacing
//  //
//  
//  /**
//   * Space before line by given amount.
//   * @return
//   */
//  public RtfHeaderStyle spaceBeforeLine( double space, RtfUnit unit )
//  {
//    parfmt.append( "\\sb" ).append( unit.toTwips( space ) );
//    return this;
//  }
//  
//  /**
//   * Space after line by given amount.
//   * @return
//   */
//  public RtfHeaderStyle spaceAfterLine( double space, RtfUnit unit )
//  {
//    parfmt.append( "\\sa" ).append( unit.toTwips( space ) );
//    return this;
//  }
//
//  /**
//   * Space between lines by a given amount.
//   * @return
//   */
//  public RtfHeaderStyle spaceBetweenLines( double space, RtfUnit unit )
//  {
//    space = Math.abs( space );
//    
//    parfmt.append( "\\sa" ).append( unit.toTwips( space ) );
//    return this;
//  }
//
//  /**
//   * Line spacing is automatically determined by the tallest character in the line.
//   * @return
//   */
//  public RtfHeaderStyle spaceBetweenLinesAutomatically()
//  {
//    parfmt.append( "\\s1000" );
//    return this;
//  }
//
//  /**
//   * Text in this paragraph will be displayed with right to left precedence.
//   * @return
//   */
//  public RtfHeaderStyle rightToLeft()
//  {
//    parfmt.append( "\\rtlpar" );
//    return this;
//  }
//  
//  /**
//   * Text in this paragraph will be displayed with left to right precedence. This is the default.
//   * @return
//   */
//  public RtfHeaderStyle leftToRight()
//  {
//    parfmt.append( "\\ltrpar" );
//    return this;
//  }  
//
//  
//  //
//  // <chrfmt>
//  //
//
//  /**
//   * Bold.
//   */
//  public RtfHeaderStyle bold()
//  {
//    chrfmt.append( "\\b" );
//    return this;
//  }  
//
//  /**
//   * Italic.
//   */
//  public RtfHeaderStyle italic()
//  {
//    chrfmt.append( "\\i" );
//    return this;
//  }  
//
//  /**
//   * All capitals.
//   */
//  public RtfHeaderStyle caps()
//  {
//    chrfmt.append( "\\caps" );
//    return this;
//  }
//
//  /**
//   * Font.
//   */
//  public RtfHeaderStyle font( int fontnumber )
//  {
//    if ( fontnumber < 0 )
//      throw new IllegalArgumentException( "Font number is not allowed to be negative" );
//
//    chrfmt.append( "\\f" ).append( fontnumber );
//    return this;
//  }
//
//  /**
//   * Font size in points. Will be doubled because intern its half-point. Default is 12.
//   */
//  public RtfHeaderStyle fontSize( int fontsize )
//  {
//    if ( fontsize < 0 )
//      throw new IllegalArgumentException( "Font size is not allowed to be negative" );
//
//    chrfmt.append( "\\fs" ).append( fontsize * 2 );
//    return this;
//  }
//
//  /**
//   * Foreground color.
//   * 
//   * @param colorindex
//   * @return
//   */
//  public RtfHeaderStyle fontColor( int colorindex )
//  {
//    if ( colorindex < 0 )
//      throw new IllegalArgumentException( "Color index is not allowed to be negative" );
//
//    chrfmt.append( "\\cf" ).append( colorindex );
//    return this;
//  }  
//  
//  //
//  
//  public RtfHeader at( int stylenum )
//  {
//    if ( stylenum < 0 )
//      throw new IllegalArgumentException( "Style number is not allowed to be negative" );
//
//    if ( parfmt.length() + chrfmt.length() == 0 )
//      throw new IllegalArgumentException( "There has to be some formattings for a style" );
//
//    this.stylenum = stylenum;
//
//    return this;
//  }
//    
//  /**
//   * Writes the style definition for the header.
//   * 
//   * @param out
//   * @throws IOException
//   */
//  void writeStyle( Appendable out ) throws IOException
//  {
//    /*
//     * <style>    := '{'
//     *               <styledef>?
//     *               <keycode>?
//     *               <formatting>
//     *               <additive>?
//     *               <based>?
//     *               <next>?
//     *               <autoupd>?
//     *               <hidden>?
//     *               <personal>?
//     *               <compose>?
//     *               <reply>?
//     *               <styleid>?
//     *               <semihidden>?
//     *               <stylename>?
//     *               ';' '}'
//     * 
//     * <styledef>   := \s | \cs | \ds                  
//     * 
//     * <formatting> := (<brdrdef> |
//     *                  <parfmt> |
//     *                  <apoctl> |
//     *                  <tabdef> |
//     *                  <shading> |
//     *                  <chrfmt>)+
//     */
//    
//    out.append( '{' );
//
//    if ( styledef.isEmpty() )  // The default style has no style number (and no name)
//    {
//      if ( stylenum != 0 )
//        throw new IllegalArgumentException( "Default style has to be at position 0" );
//    }
//    else
//      out.append( styledef ).append( Integer.toString( stylenum ) );      
//  
//    out.append( brdrdef ).append( parfmt ).append( chrfmt );
//    
//    if ( additive )
//      if ( styledef.equals( "\\cs" ) )
//        out.append( "\\additive\n" );
//
//    if ( basedOn != null )
//      out.append( basedOn );
//
//    if ( next != null )
//      out.append( next );
//    
//    out.append( '\n' );
//    Rtf.asRtf( out, stylename );
//    out.append( ";}" );
//  }
//}
