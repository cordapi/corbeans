/*
 *     Corbeans: Corda integration for Spring Boot
 *     Copyright (C) 2018 Manos Batsis
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 3 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */
package com.github.manosbatsis.corbeans.spring.boot.corda.bind

import com.github.manosbatsis.vaultaire.util.asUniqueIdentifier
import net.corda.core.contracts.UniqueIdentifier
import org.springframework.boot.jackson.JsonComponent
import org.springframework.core.convert.converter.Converter

/**
 * Custom converter for transparent String<>UniqueIdentifier binding.
 * External ID conversions are supported for strings following an ${externalId}_${id} format
 */
class StringToUniqueIdentifierConverter : Converter<String, UniqueIdentifier> {
    override fun convert(source: String): UniqueIdentifier {
        return source.asUniqueIdentifier()
    }
}

class UniqueIdentifierToStringConverter : Converter<UniqueIdentifier, String> {
    override fun convert(source: UniqueIdentifier): String {
        return source.toString()
    }
}

@JsonComponent
class UniqueIdentifierJsonSerializer : AbstractToStringSerializer<UniqueIdentifier>()

@JsonComponent
class UniqueIdentifierJsonDeserializer : AbstractFromStringDeserializer<UniqueIdentifier>() {
    override fun fromString(value: String): UniqueIdentifier {
        return value.asUniqueIdentifier()
    }
}
