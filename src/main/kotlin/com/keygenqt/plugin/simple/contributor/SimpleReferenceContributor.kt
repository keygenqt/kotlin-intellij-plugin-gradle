/*
 * Copyright 2020 Vitaliy Zarubin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keygenqt.plugin.simple.contributor

import com.intellij.openapi.util.*
import com.intellij.patterns.*
import com.intellij.psi.*
import com.intellij.util.*
import com.keygenqt.plugin.simple.annotator.SimpleAnnotator.Companion.SIMPLE_PREFIX_STR
import com.keygenqt.plugin.simple.annotator.SimpleAnnotator.Companion.SIMPLE_SEPARATOR_STR

class SimpleReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(PsiLiteralExpression::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement,
                    context: ProcessingContext): Array<PsiReference> {
                    if (element is PsiLiteralExpression) {
                        when (val value = element.value) {
                            is String -> {
                                if (value.startsWith(SIMPLE_PREFIX_STR + SIMPLE_SEPARATOR_STR)) {
                                    val property =
                                        TextRange(SIMPLE_PREFIX_STR.length + SIMPLE_SEPARATOR_STR.length + 1,
                                            value.length + 1)
                                    return arrayOf(
                                        SimpleReference(element, property))
                                }
                            }
                        }
                    }
                    return PsiReference.EMPTY_ARRAY
                }
            })
    }
}