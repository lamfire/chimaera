package com.lamfire.thalia.serializer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.lamfire.thalia.stream.DataInputOutput;

public class SerialUtils {

	/**
	 * Serialier used to insert already serialized data into store
	 */
	public static final Serializer<byte[]> FAKE_SERIALIZER = new Serializer<byte[]>() {

		public void serialize(DataOutput out, byte[] data) throws IOException {
			out.write(data);
		}

		public byte[] deserialize(DataInput in) throws IOException, ClassNotFoundException {
			throw new UnsupportedOperationException();
		}
	};

	public static <E> E fastDeser(DataInputOutput in, Serializer<E> serializer, int expectedSize) throws IOException, ClassNotFoundException {
		// we should propably copy data for deserialization into
		// separate buffer and pass it to Serializer
		// but to make it faster, Serializer will operate directly on
		// top of buffer.
		// and we check that it readed correct number of bytes.
		int origAvail = in.available();
		if (origAvail == 0) {
			throw new InternalError();
		}
		// is backed up by byte[]
		// buffer, so there
		// should be always
		// avail bytes
		E ret = serializer.deserialize(in);
		// check than valueSerializer did not read more bytes, if yes it
		// readed bytes from next record
		int readed = origAvail - in.available();
		if (readed > expectedSize) {
			throw new IOException("Serializer readed more bytes than is record size.");
		} else if (readed != expectedSize) {
			// deserializer did not readed all bytes, unussual but
			// valid.
			// Skip some to get into correct position
			for (int ii = 0; ii < expectedSize - readed; ii++) {
				in.readUnsignedByte();
			}
		}
		return ret;
	}

}
