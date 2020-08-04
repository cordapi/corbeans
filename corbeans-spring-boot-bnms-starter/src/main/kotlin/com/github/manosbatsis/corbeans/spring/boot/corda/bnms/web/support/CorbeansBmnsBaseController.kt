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
package com.github.manosbatsis.corbeans.spring.boot.corda.bnms.web.support

import com.github.manosbatsis.corbeans.spring.boot.corda.bnms.service.CordaBnmsService
import com.github.manosbatsis.corbeans.spring.boot.corda.web.CorbeansBaseController
import com.github.manosbatsis.corda.rpc.poolboy.config.NodeParams
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.util.Optional
import javax.annotation.PostConstruct

open class CorbeansBmnsBaseController : CorbeansBaseController() {

    companion object {
        private val logger = LoggerFactory.getLogger(CorbeansBmnsBaseController::class.java)
    }

    /** BMNS services by configured name */
    @Autowired(required = false)
    lateinit var bmnsServices: Map<String, CordaBnmsService<*>>

    /** Store the default node name */
    protected lateinit var defaultNodeName: String

    @PostConstruct
    fun postConstruct() {
        logger.debug("Executing postConstruct")
        if (!::bmnsServices.isInitialized) {
            logger.warn("No CordaBnmsService beans found")
            bmnsServices = emptyMap()
        }
        // if single node config, use the only node name as default, else reserve explicitly for cordform
        defaultNodeName = if (bmnsServices.keys.size == 1
                || !bmnsServices.keys.contains(NodeParams.NODENAME_CORDFORM))
            bmnsServices.keys.first().replace("NodeService", "")
        else NodeParams.NODENAME_CORDFORM
        logger.debug("Auto-configured BMNS services for Corda nodes:: {}, default node: {}",
                bmnsServices.keys, defaultNodeName)
    }

    /**
     * Get a BMNS service by name. Default is either the only node name if single,
     * or `cordform` based on node.conf otherwise
     */
    protected fun getBmnsService(optionalNodeName: Optional<String>): CordaBnmsService<*> {
        val nodeName = networkService.resolveNodeName(optionalNodeName)
        return this.bmnsServices["${nodeName}BmnsService"]
                ?: throw IllegalArgumentException("Node not found: `$optionalNodeName`")
    }

}
