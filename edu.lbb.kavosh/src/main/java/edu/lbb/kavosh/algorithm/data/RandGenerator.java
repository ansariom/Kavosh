package edu.lbb.kavosh.algorithm.data;

public class RandGenerator {
	int numOFexchange = 3;

	public int binarySearch(int[] E, int v, int w, int l, int h) {
		int m = 0;
		while (h >= l) {
			m = (l + h) / 2;
			if (E[m] == w)
				return m;
			else {
				if (E[m] < w)
					h = m - 1;
				else
					l = m + 1;
			}
		}
		return m;
	}

	public void genRandGraph_Edge(Graph g) {
		int i, j;
		int len = g.getGraphSize();
		int p, q, a, b, c = 0, d = 0, c_ind, d_ind;

		int[] Na, Nb;
		int success = 0;

		for (j = 0; j < numOFexchange; j++) {
			for (a = 1; a <= len; a++) {
				Na = g.getNeighbours(a);
				do {
					b = g.get_vertex();
				} while (a == b);

				Nb = g.getNeighbours(b);

				p = q = -1;

				for (i = 0; i < numOFexchange; i++) {
					c_ind = ((int) Math.random() % Na[0]) + 1;
					if (g.isConnected(a, Na[c_ind])
							&& !g.isConnected(b, Na[c_ind]) && Na[c_ind] != b
							&& !g.isConnected(Na[c_ind], a)) {
						c = Na[c_ind];
						p = c_ind;
						break;
					}
				}

				if (p == -1)
					continue;

				for (i = 0; i < numOFexchange; i++) {
					d_ind = ((int) Math.random() % Nb[0]) + 1;
					if (g.isConnected(b, Nb[d_ind])
							&& !g.isConnected(a, Nb[d_ind]) && Nb[d_ind] != a
							&& !g.isConnected(Nb[d_ind], b)) {
						d = Nb[d_ind];
						q = d_ind;
						break;
					}
				}

				if (q == -1)
					continue;

				success++;

				g.deleteEdgeAdjMat(a, c);
				g.deleteEdgeAdjMat(b, d);
				g.addEdgeAdjMat(a, d);
				g.addEdgeAdjMat(b, c);

				g.swapEdge(a, p, d);
				g.swapEdge(b, q, c);

				p = binarySearch(g.getNeighbours(d), d, b, 1,
						g.getNeighbours(d)[0]);
				g.swapEdge(d, p, a);
				p = binarySearch(g.getNeighbours(c), c, a, 1,
						g.getNeighbours(c)[0]);
				g.swapEdge(c, p, b);

			}
		}
	}

}
