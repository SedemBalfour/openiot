/**
*    Copyright (c) 2011-2014, OpenIoT
*   
*    This file is part of OpenIoT.
*
*    OpenIoT is free software: you can redistribute it and/or modify
*    it under the terms of the GNU Lesser General Public License as published by
*    the Free Software Foundation, version 3 of the License.
*
*    OpenIoT is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU Lesser General Public License for more details.
*
*    You should have received a copy of the GNU Lesser General Public License
*    along with OpenIoT.  If not, see <http://www.gnu.org/licenses/>.
*
*    Contact: OpenIoT mailto: info@openiot.eu
*    @author Sofiane Sarni
*    @author Hylke van der Schaaf
*/

package org.openiot.gsn.metadata.LSM;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LSMSensorMetaData {
	public final static String KEY_SENSOR_ID = "sensorID";
    private static final transient Logger logger = LoggerFactory.getLogger(LSMSensorMetaData.class);
    
    private String sensorName;
    private String author;
    private String sensorType;
    private String information;
    private String sourceType;
    private String source;
    private String featureOfInterest;
    private double latitude;
    private double longitude;
    private Map<String, LSMFieldMetaData> fields = new HashMap<String, LSMFieldMetaData>();
    String sensorID;

    public String getSensorName() {
        return sensorName;
    }

    public String getAuthor() {
        return author;
    }

    public String getSensorType() {
        return sensorType;
    }

    public String getInformation() {
        return information;
    }

    public String getSourceType() {
        return sourceType;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Map<String, LSMFieldMetaData> getFields() {
        return fields;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSensorID() {
        return sensorID;
    }

    public void setSensorID(String sensorID) {
        this.sensorID = sensorID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LSMSensorMetaData{")
                .append("\nsensorName  =").append(sensorName)
                .append("\nauthor      =").append(author)
                .append("\nsensorType  =").append(sensorType)
                .append("\ninformation =").append(information)
                .append("\nsourceType  =").append(sourceType)
                .append("\nsource      =").append(source)
                .append("\nsensorID    =").append(sensorID)
                .append("\nfields =>\n");
        for (String fieldName:fields.keySet()) {
            sb.append("\t").append(fieldName).append(" : ").append(fields.get(fieldName)).append("\n");
        }
        return sb.toString();
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

	public String getFeatureOfInterest() {
		return featureOfInterest;
	}

	public void setFeatureOfInterest(String featureOfInterest) {
		this.featureOfInterest = featureOfInterest;
	}

public void init(Config conf) {
		init(conf, false);
	}

	/**
	 * Initialize the sensor metadata from the given configuration set. If the
	 * data set does not contain a sensorId, and allowMissingSensorId is not
	 * true, an exception will be thrown. If allowMissingSensorId is true, then
	 * the sensorId should be set externally.
	 *
	 * @param conf The configuration to use for initailization.
	 * @param allowMissingSensorId Flag stating whether a missing sensorId is
	 * allowed.
	 */
	public void init(Config conf, boolean allowMissingSensorId) {
	
     
    	this.setSensorName(conf.getString("sensorName"));
        this.setAuthor(conf.getString("author"));
        this.setInformation(conf.getString("information"));
        this.setSensorType(conf.getString("sensorType"));
        this.setSourceType(conf.getString("sourceType"));
        this.setSource(conf.getString("source"));
if (conf.hasPath(KEY_SENSOR_ID)) {
			this.setSensorID(conf.getString(KEY_SENSOR_ID));
		} else {
			if (!allowMissingSensorId) {
				// This will throw an exception.
				conf.getString(KEY_SENSOR_ID);
			}
		}
        this.setLatitude(conf.getDouble("latitude"));
        this.setLongitude(conf.getDouble("longitude"));

        if (conf.hasPath("feature"))
          this.featureOfInterest=conf.getString("feature");
        /*String registeredToLSMString = PropertiesReader.readProperty(fileName, "registered");
        if (registeredToLSMString.equalsIgnoreCase("true"))
            registeredToLSM = true;
        else
            registeredToLSM = false;*/
        String listOfFieldsString = conf.getString("fields");
        String [] fieldNames = listOfFieldsString.trim().split(",");
        for (int i = 0; i < fieldNames.length; i++) {
            String fieldName = fieldNames[i];
            logger.info(i + " : " + fieldName);
            LSMFieldMetaData lsmFieldMetaData = new LSMFieldMetaData();
            lsmFieldMetaData.setGsnFieldName(fieldName);
            lsmFieldMetaData.setLsmPropertyName(conf.getString("field." + fieldName + "." + "propertyName"));
            lsmFieldMetaData.setLsmUnit(conf.getString("field." + fieldName + "." + "unit"));
            fields.put(lsmFieldMetaData.getLsmPropertyName(), lsmFieldMetaData);
            //logger.info(fields.get(fieldName));
        }
        /*String [] props = new String[fieldNames.length];
        int i=0;
        for (LSMFieldMetaData field : fields.values()){
        	props[i]=field.getLsmPropertyName();i++;
        }
        this.setProperties(props);*/

    /*} catch (NullPointerException e) {
        logger.warn("Error while reading properties file: " + fileName);
        logger.warn(e);
        return false;
    }*/

    //return true;

    }
    
    public void initFromConfigFile(String fileName) {
    	logger.debug("Read file "+fileName);
        Config conf = ConfigFactory.parseFile(new File(fileName));
        init(conf);
    }

	public String[] getProperties() {
		String [] props = new String[fields.size()];
        int i=0;
        for (LSMFieldMetaData field : fields.values()){
        	props[i]=field.getLsmPropertyName();i++;
        }
		return props;
	}
}
