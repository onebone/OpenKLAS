/*
 * OpenKLAS
 * Copyright (C) 2020-2021 OpenKLAS Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openklas.ui.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.openklas.klas.KlasUri
import org.openklas.utils.HtmlUtils

@Composable
fun SimpleHtml(html: String) {
	SimpleHtml(html = Jsoup.parse(html, KlasUri.ROOT_URI))
}

@Composable
fun SimpleHtml(html: Document) {
	Column {
		BlockElement(html.body())
	}
}

@Composable
private fun BlockElement(element: Element) {
	for(node in element.childNodes()) {
		when(node) {
			is TextNode -> Text(text = node.text())
			is Element -> when(node.tagName()) {
				"pre", "div" -> BlockElement(element = node)
				"a" -> InlineElement(content = node)
				"p" -> Paragraph(content = node)
				"br" -> LineBreak(content = node)
				else -> Text(text = node.text())
			}
		}
	}
}

@Composable
private fun Paragraph(content: Element) {
	Box(modifier = Modifier
		.fillMaxWidth()
		.wrapContentHeight()
	) {
		InlineChildrenElement(content)
	}
}

@Composable
private fun InlineElement(content: Element) {
	val text = buildAnnotatedString {
		appendHtmlInlineElement(content)
	}

	val uriHandler = LocalUriHandler.current
	ClickableText(
		text = text,
		onClick = {
			text.getStringAnnotations(TAG_URL, it, it).firstOrNull()?.let { annotation ->
				uriHandler.openUri(annotation.item)
			}
		}
	)
}

@Composable
private fun InlineChildrenElement(element: Element) {
	val text = buildAnnotatedString {
		appendHtmlInlineElementChildren(element)
	}

	val uriHandler = LocalUriHandler.current
	ClickableText(
		text = text,
		onClick = {
			text.getStringAnnotations(TAG_URL, it, it).firstOrNull()?.let { annotation ->
				uriHandler.openUri(annotation.item)
			}
		}
	)
}

@Composable
private fun LineBreak(content: Element) {
	Box(modifier = Modifier
		.fillMaxWidth()
		.wrapContentHeight()
	) {
		Text(text = buildAnnotatedString {
			appendHtmlInlineElement(content)
		})
	}
}

private const val TAG_URL = "URL"

private fun AnnotatedString.Builder.appendHtmlInlineElement(node: Node) {
	when(node) {
		is TextNode -> append(node.text())
		is Element -> {
			val appliedStyles = applyCss(node.attr("style"))

			when(node.tagName()) {
				"a" -> {
					pushStyle(SpanStyle(
						textDecoration = TextDecoration.Underline)
					)
					pushStringAnnotation(TAG_URL, node.attr("href"))
					appendHtmlInlineElementChildren(node)
					pop()
					pop()
				}
				"u" -> {
					pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
					appendHtmlInlineElementChildren(node)
					pop()
				}
				"b" -> {
					pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
					appendHtmlInlineElementChildren(node)
					pop()
				}
				"font", "span" -> appendHtmlInlineElementChildren(node)
				"br" -> append("\n")
				else -> append(node.text())
			}

			repeat(appliedStyles) {
				pop()
			}
		}
	}
}

private fun AnnotatedString.Builder.appendHtmlInlineElementChildren(element: Element) {
	for(node in element.childNodes()) {
		appendHtmlInlineElement(node)
	}
}

private fun AnnotatedString.Builder.applyCss(style: String): Int {
	var applied = 0

	val backgroundColor = HtmlUtils.extractCssEntry(style, "background(?:-color)?")
	val color = HtmlUtils.extractCssEntry(style, "color")
	val decoration = HtmlUtils.extractCssEntry(style, "text-decoration")

	if(backgroundColor != null || color != null || decoration != null) {
		++applied

		val parsedColor = HtmlUtils.parseColor(color)?.let {
			Color(it)
		} ?: Color.Unspecified

		val parsedBackgroundColor = HtmlUtils.parseColor(backgroundColor)?.let {
			Color(it)
		} ?: Color.Unspecified

		pushStyle(SpanStyle(
			color = parsedColor,
			background = parsedBackgroundColor,
			textDecoration = when(decoration) {
				"none" -> TextDecoration.None
				"line-through" -> TextDecoration.LineThrough
				"underline" -> TextDecoration.Underline
				else -> null
			}
		))
	}

	return applied
}
