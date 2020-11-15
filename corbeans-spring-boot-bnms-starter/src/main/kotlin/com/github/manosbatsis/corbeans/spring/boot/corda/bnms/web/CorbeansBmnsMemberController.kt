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
package com.github.manosbatsis.corbeans.spring.boot.corda.bnms.web

import com.github.manosbatsis.corbeans.spring.boot.corda.bnms.message.MembershipRequestMessage
import com.github.manosbatsis.corbeans.spring.boot.corda.bnms.web.support.CorbeansBmnsMemberBaseController


import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import net.corda.bn.states.BNIdentity
import net.corda.bn.states.MembershipState
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.Optional

/**
 *  Exposes BNMS member methods as endpoints for a single node or,
 *  by overriding `getRequestNodeName()`, multiple nodes.
 *
 *  To use the controller simply extend it and add a `@RestController` annotation:
 *
 *  ```
 *  @RestController
 *  class MyBmnsMemberController: CorbeansBmnsMemberController()
 *  ```
 *
 *  @see CorbeansBmnsMemberPathFragmentController
 */
@RequestMapping(path = arrayOf("api/bnms/member"))
open class CorbeansBmnsMemberController<T: BNIdentity> : CorbeansBmnsMemberBaseController<T>() {

    companion object {
        private val logger = LoggerFactory.getLogger(CorbeansBmnsMemberController::class.java)
    }

    /** Override to control how the the node name is resolved based on the request by e.g. parsing headers */
    open fun getRequestNodeName(): Optional<String> = Optional.empty()


    @PostMapping("memberships")
    @Operation(summary = "Request the BNO to kick-off the on-boarding procedure.")
    fun createMembershipRequest(@RequestBody input: MembershipRequestMessage<T>): MembershipState =
            super.createMembershipRequest(getRequestNodeName(), input)


    @PutMapping("memberships")
    @Operation(summary = "Propose a change to the membership metadata.")
    fun ammendMembershipRequest(@RequestBody input: MembershipRequestMessage<T>): MembershipState =
            super.ammendMembershipRequest(getRequestNodeName(), input)


    @GetMapping("memberships")
    @Operation(summary = "Get a memberships list from a BNO.",
            description = "Members retrieve the full list on the first invocation only. " +
                    "All subsequent updates are delivered via push notifications from the BNO. " +
                    "Memberships cache can be force-refreshed by setting forceRefresh of GetMembershipsFlow to true. " +
                    "Members that are missing from the Network Map are filtered out from the result list.")
    fun listMemberships(
            @Parameter(name = "The BNO party name")
            @RequestParam(required = true)
            bno: String,
            @Parameter(name = "The network ID")
            @RequestParam(required = false)
            networkId: Optional<String>,
            @Parameter(name = "Whether to force a refresh.")
            @RequestParam(required = false, defaultValue = "false")
            forceRefresh: Boolean,
            @Parameter(name = "Whether to filter out anyone missing from the Network Map.")
            @RequestParam(required = false, defaultValue = "true")
            filterOutMissingFromNetworkMap: Boolean
    ): List<MembershipState> =
            super.listMemberships(getRequestNodeName(), bno, networkId, forceRefresh, filterOutMissingFromNetworkMap)


}
