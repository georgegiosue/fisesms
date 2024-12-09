package xyz.ggeorge.fisesms.data.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.ggeorge.fisesms.data.dao.FiseDao
import xyz.ggeorge.fisesms.data.entities.FiseEntity

val sampleFiseData = listOf(
    FiseEntity("FISE20241009A", "70789012", 66.0, "1725715200"),
    FiseEntity("FISE20241009B", "80890123", 61.0, "1725718800"),
    FiseEntity("FISE20241009C", "90901234", 56.5, "1725722400"),
    FiseEntity("FISE20241010A", "11234567", 54.0, "1725801600"),
    FiseEntity("FISE20241010B", "12345678", 62.5, "1725805200"),
    FiseEntity("FISE20241010C", "23456789", 69.5, "1725808800"),
    FiseEntity("FISE20241011A", "34567890", 58.0, "1725888000"),
    FiseEntity("FISE20241011B", "45678901", 51.0, "1725891600"),
    FiseEntity("FISE20241011C", "56789012", 65.0, "1725895200"),
    FiseEntity("FISE20241012A", "67890123", 68.0, "1725974400"),
    FiseEntity("FISE20241012B", "78901234", 70.5, "1725978000"),
    FiseEntity("FISE20241012C", "89012345", 60.0, "1725981600"),
    FiseEntity("FISE20241013A", "90123456", 64.0, "1726060800"),
    FiseEntity("FISE20241013B", "11234567", 53.5, "1726064400"),
    FiseEntity("FISE20241013C", "22345678", 50.0, "1726068000"),
    FiseEntity("FISE20241014A", "33456789", 75.5, "1726147200"),
    FiseEntity("FISE20241014B", "44567890", 69.0, "1726150800"),
    FiseEntity("FISE20241014C", "55678901", 66.0, "1726154400"),
    FiseEntity("FISE20241015A", "66789012", 59.5, "1726233600"),
    FiseEntity("FISE20241015B", "77890123", 48.0, "1726237200"),
    FiseEntity("FISE20241015C", "88901234", 70.0, "1726240800")
)

fun populateDatabase(fiseDao: FiseDao) {
    CoroutineScope(Dispatchers.IO).launch {
        fiseDao.insertAll(*sampleFiseData.toTypedArray())
    }
}