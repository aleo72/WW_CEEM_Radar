/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.utils.sector

import gov.nasa.worldwind.geom.Sector

/**
 * Объект в котором собраны все методы утилиты
 *
 * Created by Aleo on 03.02.14.
 */
object SectorUtils {

	def makeSectorDescription(sector: Sector): String = {
		"S %7.4f° W %7.4f° N %7.4f° E %7.4f°" format (
			sector.getMinLatitude.degrees,
			sector.getMinLongitude.degrees,
			sector.getMaxLatitude.degrees,
			sector.getMaxLongitude.degrees
		)
	}

	def makeSizeDescription(size: Long): String = {
		val sizeInMegaBytes = size / 1024.0 / 1024.0
		if(sizeInMegaBytes < 1024.0)
			"%,.1f MB".format(sizeInMegaBytes)
		else if (sizeInMegaBytes < (1024 * 1024) )
			"%,.1f GB".format(sizeInMegaBytes / 1024.0)
		else
			"%,.1f TB".format(sizeInMegaBytes / 1024.0 / 1024.0)

	}
}
