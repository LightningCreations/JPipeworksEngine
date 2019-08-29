package com.lightning.jpipeworks.collision;

import com.lightning.jpipeworks.things.PositionedThing;

public class CollisionMath extends Collision {
	public static class Operation {
		// because writing true/false is cumbersome:
		private static final boolean f = false;
		private static final boolean t = true;
		
		// Basic types
		public static final Operation OR                 = new Operation(f, f, f, f);
		public static final Operation OR_NOTA            = new Operation(f, t, f, f);
		public static final Operation OR_NOTB            = new Operation(f, f, t, f);
		public static final Operation OR_NOTA_NOTB       = new Operation(f, t, t, f);
		public static final Operation OR_NOTC            = new Operation(f, f, f, t);
		public static final Operation OR_NOTA_NOTC       = new Operation(f, t, f, t);
		public static final Operation OR_NOTB_NOTC       = new Operation(f, f, t, t);
		public static final Operation OR_NOTA_NOTB_NOTC  = new Operation(f, t, t, t);
		
		public static final Operation XOR                = new Operation(t, f, f, f);
		public static final Operation XOR_NOT            = new Operation(t, f, f, t);
		public static final Operation XOR_NOTA           = XOR_NOT;
		public static final Operation XOR_NOTB           = XOR_NOT;
		public static final Operation XOR_NOTA_NOTB      = XOR;
		public static final Operation XOR_NOTC           = XOR_NOT;
		public static final Operation XOR_NOTA_NOTC      = XOR;
		public static final Operation XOR_NOTB_NOTC      = XOR;
		public static final Operation XOR_NOTA_NOTB_NOTC = XOR_NOT;
		
		// Aliases
		public static final Operation AND                 = OR_NOTA_NOTB_NOTC;
		public static final Operation AND_NOTA            = OR_NOTB_NOTC;
		public static final Operation AND_NOTB            = OR_NOTA_NOTC;
		public static final Operation AND_NOTA_NOTB       = OR_NOTC;
		public static final Operation AND_NOTC            = OR_NOTA_NOTB;
		public static final Operation AND_NOTA_NOTC       = OR_NOTB;
		public static final Operation AND_NOTB_NOTC       = OR_NOTA;
		public static final Operation AND_NOTA_NOTB_NOTC  = OR;
		
		public static final Operation NAND                = OR_NOTA_NOTB;
		public static final Operation NAND_NOTA           = OR_NOTB;
		public static final Operation NAND_NOTB           = OR_NOTA;
		public static final Operation NAND_NOTA_NOTB      = OR;
		public static final Operation NAND_NOTC           = OR_NOTA_NOTB_NOTC;
		public static final Operation NAND_NOTA_NOTC      = OR_NOTB_NOTC;
		public static final Operation NAND_NOTB_NOTC      = OR_NOTA_NOTC;
		public static final Operation NAND_NOTA_NOTB_NOTC = OR_NOTC;
		
		public static final Operation NOR                 = OR_NOTC;
		public static final Operation NOR_NOTA            = OR_NOTA_NOTC;
		public static final Operation NOR_NOTB            = OR_NOTB_NOTC;
		public static final Operation NOR_NOTA_NOTB       = OR_NOTA_NOTB_NOTC;
		public static final Operation NOR_NOTC            = OR;
		public static final Operation NOR_NOTA_NOTC       = OR_NOTA;
		public static final Operation NOR_NOTB_NOTC       = OR_NOTB;
		public static final Operation NOR_NOTA_NOTB_NOTC  = OR_NOTA_NOTB;

		public static final Operation XNOR                = XOR_NOT;
		public static final Operation XNOR_NOT            = XOR;

		private boolean x;  // t:XOR f:OR
		private boolean iA; // t:!A  f:A
		private boolean iB; // t:!B  f:B
		private boolean iC; // t:!C  f:C
		
		Operation(boolean x, boolean iA, boolean iB, boolean iC) {
			this.x = x;
			this.iA = iA;
			this.iB = iB;
			this.iC = iC;
		}
		
		public boolean eval(Collision a, Collision b, Collision other) {
			boolean A = iA ? !a.collide(other) : a.collide(other);
			if(!x && !A^iC) return false;
			boolean B = iB ? !b.collide(other) : b.collide(other);
			boolean c = x ? A^B : A||B;
			return iC ? !c : c;
		}
	}
	
	private Collision a;
	private Collision b;
	private Operation op;
	
	public CollisionMath(Collision a, Collision b, Operation op) {
		super(a.getAnchor(), 0, 0);
		this.a = a;
		this.b = b;
		this.op = op;
	}

	@Override
	public boolean collide(Collision other) {
		return op.eval(a, b, other);
	}
}
