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
package com.github.manosbatsis.corbeans.spring.boot.corda.service

import com.github.manosbatsis.corda.rpc.poolboy.PoolBoyConnection
import com.github.manosbatsis.vaultaire.plugin.accounts.service.node.AccountsAwareNodeServicePoolBoyDelegate
import com.github.manosbatsis.vaultaire.plugin.accounts.service.node.BasicAccountsAwareNodeService
import com.github.manosbatsis.vaultaire.service.ServiceDefaults
import com.github.manosbatsis.vaultaire.service.SimpleServiceDefaults
import com.github.manosbatsis.vaultaire.service.node.BasicNodeService
import com.github.manosbatsis.vaultaire.service.node.NodeServiceRpcPoolBoyDelegate
import net.corda.core.identity.Party
import net.corda.core.node.services.vault.QueryCriteria
import org.slf4j.LoggerFactory


/**
 *  Abstract base implementation for RPC-based node services.
 *
 */
abstract class CordaRpcServiceBase(
        override val delegate: NodeServiceRpcPoolBoyDelegate
) : BasicNodeService(delegate), CordaNodeService {

    companion object {
        private val logger = LoggerFactory.getLogger(CordaRpcServiceBase::class.java)
    }
    /** [PoolBoyConnection]-based constructor */
    constructor(
            poolBoy: PoolBoyConnection
    ) : this(NodeServiceRpcPoolBoyDelegate(poolBoy))

    override val myIdentity: Party by lazy { nodeIdentity }

    protected val myIdCriteria: QueryCriteria.LinearStateQueryCriteria by lazy {
        QueryCriteria.LinearStateQueryCriteria(participants = listOf(nodeIdentity))

    }
}

/**
 *  Abstract accounts-aware base implementation for RPC-based node services.
 *
 */
abstract class CordaAccountsAwareRpcServiceBase(
        override val delegate: AccountsAwareNodeServicePoolBoyDelegate
) : BasicAccountsAwareNodeService(delegate), CordaNodeService {

    companion object {
        private val logger = LoggerFactory.getLogger(CordaRpcServiceBase::class.java)
    }
    /** [PoolBoyConnection]-based constructor */
    constructor(
            poolBoy: PoolBoyConnection
    ) : this(AccountsAwareNodeServicePoolBoyDelegate(poolBoy))

    override val myIdentity: Party by lazy { nodeIdentity }

    protected val myIdCriteria: QueryCriteria.LinearStateQueryCriteria by lazy {
        QueryCriteria.LinearStateQueryCriteria(participants = listOf(nodeIdentity))

    }
}
