package org.grade.client.upload;

import static java.lang.String.*;
import static java.util.logging.Logger.*;
import static javax.ws.rs.client.ClientBuilder.*;
import static javax.ws.rs.core.MediaType.*;
import static lombok.AccessLevel.*;
import static org.grade.client.upload.Constants.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.message.GZipEncoder;
import org.grade.client.upload.csv.Csv;
import org.grade.client.upload.dsl.Dsl.TypeClause;

@Setter(PRIVATE)
public class Grade {
	
	public static UploadType xml = new UploadType.Default("xml",APPLICATION_XML_TYPE);
	public static UploadType json = new UploadType.Default("json",APPLICATION_JSON_TYPE);

	public static Csv csv() { 
		return new Csv();
	}
	
	
	@SneakyThrows
	public static TypeClause drop(@NonNull String file) {
		
		return drop(new FileInputStream(new File(file)));
		
	}
	
	public static TypeClause drop(InputStream c) {
		
		Grade $ = new Grade();
		
		$.content(c);
		
		return (type) -> {
			
			if (!(type instanceof UploadType.Private))
				throw new IllegalArgumentException("not an expected upload type");
			
			$.info((UploadType.Private) type);
			
			return (dpl)-> {
				
				$.deployment(dpl);

				return $::in; 
			};
		};
	}
	
	
	
	
	
	
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@NonNull
	private Deployment deployment;
	
	@NonNull
	private UploadType.Private info; 
	
	@NonNull
	private InputStream content;

	
	private static ClientBuilder maker = newBuilder()
			.register(MultiPartFeature.class)
			.register(GZipEncoder.class)
			.register(new LoggingFilter(getLogger(LoggingFilter.class.getName()),true));

	
	private static String api_template = base_path+"/%s/dropin/%s/%s";


	@SneakyThrows
	private void in(@NonNull String label) {
	
		@Cleanup //close content stream in all cases
		InputStream closeable = content;
	
		String path = format(api_template,deployment.endpoint(),info.path(),label);
		
		Response response = maker.build().target(deployment.uri()).path(path).request().post(info.bodyWith(closeable));
		
		if (!response.getStatusInfo().getFamily().equals(Family.SUCCESSFUL))
			throw new WebApplicationException(response);
	
		
	}
}