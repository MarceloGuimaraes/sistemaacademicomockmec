package br.com.pucminas.projeto.service;

public class Header {

	public static final int SIZE_HEADER = 6;
	public static final int CID = 0Xabcd;
	public static int seq = 0;

	public static int get(byte[] hdr, int off, int t) {
		int i = 0;
		while (t > 0) {
			i = (i << 8) | (hdr[off] & 0xff);
			off++;
			t--;
		}
		return i;
	}

	public static void set(byte[] hdr, int off, int t, int v) {
		off += t - 1;
		while (t > 0) {
			hdr[off] = (byte) (v & 0xff);
			v >>= 8;
			t--;
			off--;
		}

	}

	public static byte[] createHeader(int szMsg) {
		byte[] hdr = new byte[SIZE_HEADER];
		set(hdr, 0, 2, CID); // cid
		seq++;
		int i = seq % 65535;
		if (i == 0) {
			i = 65535;
		}
		int seqCcf = 1;
		set(hdr, 2, 2, seqCcf); // seq
		set(hdr, 4, 2, szMsg);
		return hdr;
	}

	public static int getSize(byte[] hdr) {
		return get(hdr, 4, 2);
	}
	
}
