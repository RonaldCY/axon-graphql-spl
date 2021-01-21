package org.ha.spl.query

import org.ha.spl.query.dashboard.Hospital
import org.ha.spl.query.dashboard.Ward
import org.ha.spl.query.hospital.HospitalView
import org.ha.spl.query.patient.PatientView
import org.ha.spl.query.ward.WardView
import org.springframework.data.jpa.repository.JpaRepository

interface HospitalViewRepository : JpaRepository<HospitalView, String>
interface WardViewRepository : JpaRepository<WardView, String>
interface PatientViewRepository : JpaRepository<PatientView, String>

interface HospitalRepository : JpaRepository<Hospital, String>
interface WardRepository : JpaRepository<Ward, String>