package org.ha.spl.api

import lombok.NoArgsConstructor

@NoArgsConstructor
data class CreateHospitalDTO(var hospCode: String)

@NoArgsConstructor
data class AddWardDTO(var wardCode: String)

@NoArgsConstructor
data class AddBedDTO(var bedNum: Int)

@NoArgsConstructor
data class CreatePatientDTO(var hkid: String, var name: String)

@NoArgsConstructor
data class CheckInPatientDTO(var hkid: String, var bedNum: Int)

// View only
data class HospitalDTO(var hospCode: String, var wards: List<String>)