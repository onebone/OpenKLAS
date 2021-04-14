package org.openklas.ui.shared

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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import org.openklas.klas.KlasUri

@Composable
fun SimpleHtml(html: String) {
	SimpleHtml(html = Jsoup.parse(html, KlasUri.ROOT_URI))
}

@Composable
fun SimpleHtml(html: Document) {
	Column {
		SingleElement(html.body())
	}
}

@Composable
fun SingleElement(element: Element) {
	for(node in element.childNodes()) {
		when(node) {
			is TextNode -> Text(text = node.text())
			is Element -> when (node.tagName()) {
				"p" -> Paragraph(content = node)
				"br" -> LineBreak(content = node)
				"div" -> SingleElement(element = node)
				else -> Text(text = node.text())
			}
		}
	}
}

@Composable
fun Paragraph(content: Element) {
	Box(modifier = Modifier
		.fillMaxWidth()
		.wrapContentHeight()
	) {
		Text(text = buildAnnotatedString {
			appendHtmlInlineElement(content)
		})
	}
}

@Composable
fun LineBreak(content: Element) {
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

fun AnnotatedString.Builder.appendHtmlInlineElement(element: Element) {
	for(node in element.childNodes()) {
		when(node) {
			is TextNode -> append(node.text())
			is Element -> when (node.tagName()) {
				"a" -> {
					pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
					pushStringAnnotation(TAG_URL, node.attr("href"))
					appendHtmlInlineElement(node)
					pop()
					pop()
				}
				"br" -> append("\n")
				else -> append(node.text())
			}
		}
	}
}
