package org.xomda.shared.util;

import java.util.Comparator;
import java.util.Objects;

public class AmbitiousComparator<T> implements Comparator<T> {

	private final Comparator<T> real;
	private final boolean ambition;
	private final T beneficiary;

	@SuppressWarnings("unchecked")
	public AmbitiousComparator(T clazz, boolean ambition, Comparator<? super T> real) {
		this.beneficiary = clazz;
		this.ambition = ambition;
		this.real = (Comparator<T>) real;
	}

	@Override
	public int compare(final T a, final T b) {
		if (a == beneficiary) {
			return b == beneficiary ? 0 : (this.ambition ? -1 : 1);
		} else if (b == beneficiary) {
			return this.ambition ? 1 : -1;
		} else {
			return this.real == null ? 0 : this.real.compare(a, b);
		}
	}

	public Comparator<T> thenComparing(Comparator<? super T> other) {
		Objects.requireNonNull(other);
		return new AmbitiousComparator<T>(beneficiary, this.ambition, this.real == null ? null : this.real.thenComparing(other));
	}

	public Comparator<T> reversed() {
		return new AmbitiousComparator<>(beneficiary, !this.ambition, this.real == null ? null : this.real.reversed());
	}

	public static <T> AmbitiousComparator<T> meFirst(T beneficiary, Comparator<? super T> comparator) {
		return new AmbitiousComparator<>(beneficiary, true, comparator);
	}

	public static <T> AmbitiousComparator<T> meLast(T beneficiary, Comparator<? super T> comparator) {
		return new AmbitiousComparator<>(beneficiary, false, comparator);
	}

}