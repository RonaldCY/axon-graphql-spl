package org.ha.spl.api

import org.axonframework.modelling.command.TargetAggregateIdentifier

// Hospital
data class CreateHospitalCommand(@TargetAggregateIdentifier var hospCode: String)
data class AddWardCommand(@TargetAggregateIdentifier var hospCode: String, var wardCode: String)

// Ward
data class AddBedCommand(@TargetAggregateIdentifier var wardId: String, var bedNum: Int)
data class CheckInPatientCommand(@TargetAggregateIdentifier var wardId: String, var bedNum: Int, var hkid: String, var name: String)

// Patient
data class CreatePatientCommand(@TargetAggregateIdentifier var hkid: String, var name: String)
data class CheckInWardCommand(@TargetAggregateIdentifier var hkid: String, var hospCode: String, var wardCode: String, var bedNum: Int)
data class CancelCheckInWardCommand(@TargetAggregateIdentifier var hkid: String)

